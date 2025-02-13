/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import Ice.ObjectAdapter;
import IceGrid.AdapterObserverPrx;
import IceGrid.AdapterObserverPrxHelper;
import IceGrid.AdminPrx;
import IceGrid.AdminSessionPrx;
import IceGrid.ApplicationObserverPrx;
import IceGrid.ApplicationObserverPrxHelper;
import IceGrid.NodeObserverPrx;
import IceGrid.NodeObserverPrxHelper;
import IceGrid.ObjectObserverPrx;
import IceGrid.ObjectObserverPrxHelper;
import IceGrid.ObserverAlreadyRegisteredException;
import IceGrid.RegistryObserverPrx;
import IceGrid.RegistryObserverPrxHelper;

import com.morefun.XSanGo.CenterServer;

class IceGridSession {

	private class Session {

		Session(AdminSessionPrx session, long keepAliveperiod, boolean routed) {
			_session = session;

			try {
				_admin = _session.getAdmin();
			} catch (Ice.LocalException e) {
				logout(true);
				throw e;
			}

			try {
				if (!routed) {
					Ice.ObjectPrx adminCallbackTemplate = _session
							.getAdminCallbackTemplate();

					if (adminCallbackTemplate != null) {
						_adminCallbackCategory = adminCallbackTemplate
								.ice_getIdentity().category;

						String publishedEndpoints = null;
						for (Ice.Endpoint endpoint : adminCallbackTemplate
								.ice_getEndpoints()) {
							String endpointString = endpoint.toString();
							if (publishedEndpoints == null) {
								publishedEndpoints = endpointString;
							} else {
								publishedEndpoints += ":" + endpointString;
							}
						}
					}
				}
				_serverAdminCategory = _admin.getServerAdminCategory();
			} catch (Ice.OperationNotExistException e) {
				logout(true);

				throw e;

			} catch (Ice.LocalException e) {
				logout(true);
				throw e;
			}

			_thread = new Pinger(_session, keepAliveperiod);
			_thread.setDaemon(true);
			_thread.start();

			try {
				registerObservers(routed);
			} catch (Ice.LocalException e) {
				logout(true);

				throw e;
			}
		}

		void logout(boolean destroySession) {
			close(destroySession);
			// _coordinator.sessionLost();
			_connectedToMaster = false;
			_replicaName = "";
		}

		AdminSessionPrx getSession() {
			return _session;
		}

		AdminPrx getAdmin() {
			return _admin;
		}

		String getServerAdminCategory() {
			return _serverAdminCategory;
		}

		AdminPrx getRoutedAdmin() {
			assert _admin != null;

			if (_routedAdmin == null) {
				//
				// Create a local Admin object used to route some operations to
				// the real
				// Admin.
				// Routing admin calls is even necessary when we don't through
				// Glacier2
				// since the Admin object provided by the registry is a
				// well-known object
				// (indirect, locator-dependent).
				//
				Ice.ObjectAdapter adminRouterAdapter = IceEntry
						.instance()
						.getCommunicator()
						.createObjectAdapterWithEndpoints(
								"IceGrid.AdminRouter", "tcp -h localhost");

				// _routedAdmin = AdminPrxHelper.uncheckedCast(
				// adminRouterAdapter.addWithUUID(new AdminRouter(_admin)));

				adminRouterAdapter.activate();
			}
			return _routedAdmin;
		}

		Ice.ObjectPrx addCallback(Ice.Object servant, String name, String facet) {
			if (_adminCallbackCategory == null) {
				return null;
			} else {
				return _adapter.addFacet(servant, new Ice.Identity(name,
						_adminCallbackCategory), facet);
			}
		}

		Ice.ObjectPrx addCallback(Ice.Object servant) {
			return _adapter.addWithUUID(servant);

		}

		Ice.ObjectPrx retrieveCallback(String name, String facet) {
			if (_adminCallbackCategory == null) {
				return null;
			} else {
				Ice.Identity ident = new Ice.Identity(name,
						_adminCallbackCategory);
				if (_adapter.findFacet(ident, facet) == null) {
					return null;
				} else {
					return _adapter.createProxy(ident).ice_facet(facet);
				}
			}
		}

		Ice.Object removeCallback(String name, String facet) {
			if (_adminCallbackCategory == null) {
				return null;
			} else {
				return _adapter.removeFacet(new Ice.Identity(name,
						_adminCallbackCategory), facet);
			}
		}

		void close(boolean destroySession) {
			if (_thread != null) {
				_thread.done();
			}

			if (_adapter != null) {
				new Thread() {
					public void run() {

						ObjectAdapter adapter = null;
						synchronized (this) {
							adapter = _adapter;
							_adapter = null;

						}

						try {
							adapter.destroy();

							System.out.println("adapter destroyed");
						} catch (Throwable e) {
							e.printStackTrace();
						}

					};

				}.start();

			}

			if (destroySession) {
				// _coordinator.destroySession(_session);
			}
			// _coordinator.setConnected(false);
		}

		private void registerObservers(boolean routed) {
			//
			// Create the object adapter for the observers
			//
			String category;

			if (!routed) {
				category = "observer";

				String adapterName = IceEntry.instance().getCommunicator()
						.getProperties().getProperty("Ice.Admin.ServerId")
						+ ".IceAdmin";

				_adapter = IceEntry.instance().getCommunicator()
						.createObjectAdapter(adapterName);

				_adapter.activate();
				_session.ice_getConnection().setAdapter(_adapter);
			} else {
				Glacier2.RouterPrx router = Glacier2.RouterPrxHelper
						.uncheckedCast(IceEntry.instance().getCommunicator()
								.getDefaultRouter());
				category = router.getCategoryForClient();
				// _adminCallbackCategory = category;

				_adapter = IceEntry.instance().getCommunicator()
						.createObjectAdapterWithRouter("RoutedAdapter", router);
				_adapter.activate();
			}

			//
			// Create servants and proxies
			//
			_applicationObserverIdentity.name = "application-"
					+ java.util.UUID.randomUUID().toString();
			_applicationObserverIdentity.category = category;
			_adapterObserverIdentity.name = "adapter-"
					+ java.util.UUID.randomUUID().toString();
			_adapterObserverIdentity.category = category;
			_objectObserverIdentity.name = "object-"
					+ java.util.UUID.randomUUID().toString();
			_objectObserverIdentity.category = category;
			_registryObserverIdentity.name = "registry-"
					+ java.util.UUID.randomUUID().toString();
			_registryObserverIdentity.category = category;
			_nodeObserverIdentity.name = "node-"
					+ java.util.UUID.randomUUID().toString();
			_nodeObserverIdentity.category = category;

			ApplicationObserverI applicationObserverServant = new ApplicationObserverI(
					_admin.ice_getIdentity().category);

			ApplicationObserverPrx applicationObserver = ApplicationObserverPrxHelper
					.uncheckedCast(_adapter.add(applicationObserverServant,
							_applicationObserverIdentity));

			AdapterObserverPrx adapterObserver = AdapterObserverPrxHelper
					.uncheckedCast(_adapter.add(new AdapterObserverI(),
							_adapterObserverIdentity));

			ObjectObserverPrx objectObserver = ObjectObserverPrxHelper
					.uncheckedCast(_adapter.add(new ObjectObserverI(),
							_objectObserverIdentity));

			RegistryObserverPrx registryObserver = RegistryObserverPrxHelper
					.uncheckedCast(_adapter.add(new RegistryObserverI(),
							_registryObserverIdentity));

			NodeObserverPrx nodeObserver = NodeObserverPrxHelper
					.uncheckedCast(_adapter.add(new NodeObserverI(),
							_nodeObserverIdentity));

			try {
				if (routed) {
					_session.setObservers(registryObserver, nodeObserver,
							applicationObserver, adapterObserver,
							objectObserver);
				} else {
					_session.setObserversByIdentity(_registryObserverIdentity,
							_nodeObserverIdentity,
							_applicationObserverIdentity,
							_adapterObserverIdentity, _objectObserverIdentity);
				}
			} catch (ObserverAlreadyRegisteredException ex) {
				assert false; // We use UUIDs for the observer identities.
			}

		}

		private final AdminSessionPrx _session;

		private Pinger _thread;

		private Ice.ObjectAdapter _adapter;
		private AdminPrx _admin;
		private String _serverAdminCategory;
		private String _adminCallbackCategory;
		private AdminPrx _routedAdmin;
		private Ice.Identity _applicationObserverIdentity = new Ice.Identity();
		private Ice.Identity _adapterObserverIdentity = new Ice.Identity();
		private Ice.Identity _objectObserverIdentity = new Ice.Identity();
		private Ice.Identity _registryObserverIdentity = new Ice.Identity();
		private Ice.Identity _nodeObserverIdentity = new Ice.Identity();
	}

	//
	// We create a brand new Pinger thread for each session
	//
	class Pinger extends Thread {
		Pinger(AdminSessionPrx session, long period) {
			super("Pinger");

			_session = session;
			_period = period;

			if (_period <= 0) {
				_period = 5000;
			}
		}

		public void run() {
			boolean done = false;

			do {
				synchronized (this) {
					done = _done;
				}

				if (!done) {
					try {
						_session.keepAlive();
					} catch (final Exception e) {
						synchronized (this) {
							done = _done;
							_done = true;
						}

						if (!done) {
							CenterServer.execute(new Runnable() {
								public void run() {
									sessionLost("Failed to contact the IceGrid registry: "
											+ e.toString());
								}
							});
						}
					}
				}

				synchronized (this) {
					if (!_done) {
						try {
							wait(_period);
						} catch (InterruptedException e) {
							// Ignored
						}
					}
					done = _done;
				}
			} while (!done);
		}

		public synchronized void done() {
			if (!_done) {
				_done = true;
				notify();
			}
		}

		private AdminSessionPrx _session;
		private long _period;
		private boolean _done = false;
	}

	//
	// Runs in UI thread
	//
	void createSession() {
		// _loginInfo = new LoginInfo(_loginPrefs, _coordinator);
		// _loginDialog.showDialog();
	}

	void relog(boolean showDialog) {
		// if(_loginInfo == null)
		// {
		// createSession();
		// }
		// else
		// {
		// if(showDialog || !login(_coordinator.getMainFrame()))
		// {
		// _loginDialog.showDialog();
		// }
		// }
	}

	public boolean login(AdminSessionPrx session) {
		if (_session != null) {
			// logout(true);
			return false;
		}
		assert _session == null;

		Ice.LongHolder keepAlivePeriodHolder = new Ice.LongHolder();

		if (session == null) {
			return false;
		}
		try {
			_replicaName = session.getReplicaName();
		} catch (Ice.LocalException e) {
			logout(true);
			return false;
		}

		_connectedToMaster = _replicaName.equals("Master");

		try {
			_session = new Session(session, keepAlivePeriodHolder.value, false);
		} catch (Ice.LocalException e) {
			return false;
		}

		return true;
	}

	void sessionLost(String message) {
		logout(false);
	}

	void logout(boolean destroySession) {
		System.out.println("loginout begin");
		if (_session != null) {
			_session.logout(destroySession);
			_session = null;
		}
		System.out.println("loginout end");

	}

	AdminSessionPrx getSession() {
		return _session == null ? null : _session.getSession();
	}

	AdminPrx getAdmin() {
		return _session == null ? null : _session.getAdmin();
	}

	String getServerAdminCategory() {
		return _session == null ? null : _session.getServerAdminCategory();
	}

	Ice.ObjectPrx addCallback(Ice.Object servant, String name, String facet) {
		return _session == null ? null : _session.addCallback(servant, name,
				facet);
	}

	Ice.ObjectPrx addCallback(Ice.Object servant) {
		return _session == null ? null : _session.addCallback(servant);
	}

	Ice.ObjectPrx retrieveCallback(String name, String facet) {
		return _session == null ? null : _session.retrieveCallback(name, facet);
	}

	Ice.Object removeCallback(String name, String facet) {
		return _session == null ? null : _session.removeCallback(name, facet);
	}

	AdminPrx getRoutedAdmin() {
		return _session == null ? null : _session.getRoutedAdmin();
	}

	boolean connectedToMaster() {
		return _session != null && _connectedToMaster;
	}

	String getReplicaName() {
		return _replicaName;
	}

	private Session _session;
	private boolean _connectedToMaster = false;
	private String _replicaName = "";

}
