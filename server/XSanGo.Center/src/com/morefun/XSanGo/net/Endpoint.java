/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.util.List;

/**
 * @author lusongjie
 *
 */
public class Endpoint {
	public String ip;
	public int    port;
	
	
	
	/**
	 * @param str
	 * @return
	 */
	public static Endpoint parseEndpoint(String str) {

		str = str.replaceFirst("tcp", "");

		Endpoint endpoint = new Endpoint();
		String[] arr = str.split("[ \t\n\r]+");

		int i = 0;
		while (i < arr.length) {
			if (arr[i].length() == 0) {
				i++;
				continue;
			}

			String option = arr[i++];
			if (option.length() != 2 || option.charAt(0) != '-') {
				throw new Ice.EndpointParseException(
						"expected an endpoint option but found `" + option
								+ "' in endpoint `tcp " + str + "'");
			}

			String argument = null;
			if (i < arr.length && arr[i].charAt(0) != '-') {
				argument = arr[i++];
				if (argument.charAt(0) == '\"'
						&& argument.charAt(argument.length() - 1) == '\"') {
					argument = argument.substring(1, argument.length() - 1);
				}
			}

			switch (option.charAt(1)) {
			case 'h': {
				if (argument == null) {
					throw new Ice.EndpointParseException(
							"no argument provided for -h option in endpoint `tcp "
									+ str + "'");
				}

				endpoint.ip = argument;
				break;
			}

			case 'p': {
				if (argument == null) {
					throw new Ice.EndpointParseException(
							"no argument provided for -p option in endpoint `tcp "
									+ str + "'");
				}

				try {
					endpoint.port = Integer.parseInt(argument);
				} catch (NumberFormatException ex) {
					throw new Ice.EndpointParseException("invalid port value `"
							+ argument + "' in endpoint `tcp " + str + "'");
				}

				if (endpoint.port < 0 || endpoint.port > 65535) {
					throw new Ice.EndpointParseException("port value `"
							+ argument + "' out of range in endpoint `tcp "
							+ str + "'");
				}

				break;
			}

			case 't': {
				if (argument == null) {
					throw new Ice.EndpointParseException(
							"no argument provided for -t option in endpoint `tcp "
									+ str + "'");
				}

				try {
					// _timeout = Integer.parseInt(argument);
				} catch (NumberFormatException ex) {
					throw new Ice.EndpointParseException(
							"invalid timeout value `" + argument
									+ "' in endpoint `tcp " + str + "'");
				}

				break;
			}

			case 'z': {
				if (argument != null) {
					throw new Ice.EndpointParseException(
							"unexpected argument `" + argument
									+ "' provided for -z option in `tcp " + str
									+ "'");
				}

				// _compress = true;
				break;
			}

			default: {
				throw new Ice.EndpointParseException("unknown option `"
						+ option + "' in `tcp " + str + "'");
			}
			}
		}
		return endpoint;

	}
	

	/**
	 * @param endpts
	 * @param oaEndpoints
	 * @return
	 */
	public static List<Endpoint> parseEndpoints(String endpts, boolean oaEndpoints) {
		int beg;
		int end = 0;

		final String delim = " \t\n\r";

		java.util.List<Endpoint> endpoints = new java.util.ArrayList<Endpoint>();
		while (end < endpts.length()) {
			beg = IceUtilInternal.StringUtil.findFirstNotOf(endpts, delim, end);
			if (beg == -1) {
				break;
			}

			end = beg;
			while (true) {
				end = endpts.indexOf(':', end);
				if (end == -1) {
					end = endpts.length();
					break;
				} else {
					boolean quoted = false;
					int quote = beg;
					while (true) {
						quote = endpts.indexOf('\"', quote);
						if (quote == -1 || end < quote) {
							break;
						} else {
							quote = endpts.indexOf('\"', ++quote);
							if (quote == -1) {
								break;
							} else if (end < quote) {
								quoted = true;
								break;
							}
							++quote;
						}
					}
					if (!quoted) {
						break;
					}
					++end;
				}
			}

			if (end == beg) {
				++end;
				continue;
			}

			String s = endpts.substring(beg, end);

			Endpoint endp = parseEndpoint(s);

			if (endp == null) {
				Ice.EndpointParseException e = new Ice.EndpointParseException();
				e.str = "invalid object adapter endpoint `" + s + "'";
				throw e;
			}
			endpoints.add(endp);

			++end;
		}

		return endpoints;
	}

	//
	// public Endpoint
	// create(String str, boolean oaEndpoint)
	// {
	// String s = str.trim();
	// if(s.length() == 0)
	// {
	// Ice.EndpointParseException e = new Ice.EndpointParseException();
	// e.str = "value has no non-whitespace characters";
	// throw e;
	// }
	//
	// java.util.regex.Pattern p =
	// java.util.regex.Pattern.compile("([ \t\n\r]+)|$");
	// java.util.regex.Matcher m = p.matcher(s);
	// boolean b = m.find();
	// assert(b);
	//
	// String protocol = s.substring(0, m.start());
	//
	// if(protocol.equals("default"))
	// {
	// protocol = "tcp";
	// }
	//
	// for(int i = 0; i < _factories.size(); i++)
	// {
	// EndpointFactory f = _factories.get(i);
	// if(f.protocol().equals(protocol))
	// {
	// return f.create(s.substring(m.end()), oaEndpoint);
	//
	// // Code below left in place for debugging.
	//
	// /*
	// EndpointI e = f.create(s.substring(m.end()), oaEndpoint);
	// BasicStream bs = new BasicStream(_instance, true, false);
	// e.streamWrite(bs);
	// java.nio.ByteBuffer buf = bs.getBuffer();
	// buf.position(0);
	// short type = bs.readShort();
	// EndpointI ue = new IceInternal.OpaqueEndpointI(type, bs);
	// System.err.println("Normal: " + e);
	// System.err.println("Opaque: " + ue);
	// return e;
	// */
	// }
	// }
	//
	// //
	// // If the stringified endpoint is opaque, create an unknown endpoint,
	// // then see whether the type matches one of the known endpoints.
	// //
	// if(protocol.equals("opaque"))
	// {
	// EndpointI ue = new OpaqueEndpointI(s.substring(m.end()));
	// for(int i = 0; i < _factories.size(); i++)
	// {
	// EndpointFactory f = _factories.get(i);
	// if(f.type() == ue.type())
	// {
	// //
	// // Make a temporary stream, write the opaque endpoint data into the
	// stream,
	// // and ask the factory to read the endpoint data from that stream to
	// create
	// // the actual endpoint.
	// //
	// BasicStream bs = new BasicStream(_instance, true, false);
	// ue.streamWrite(bs);
	// Buffer buf = bs.getBuffer();
	// buf.b.position(0);
	// bs.readShort(); // type
	// return f.read(bs);
	// }
	// }
	// return ue; // Endpoint is opaque, but we don't have a factory for its
	// type.
	// }
	//
	// return null;
	// }

}
