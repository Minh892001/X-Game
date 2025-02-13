using System;

namespace XSanGo.Client.Net
{
    class SessionPingThread
    {
        public event EventHandler ConnectionLost;
        private Glacier2.RouterPrx _router;
        private long _period;
        private bool _done = false;
        private readonly IceUtilInternal.Monitor _m = new IceUtilInternal.Monitor();

        public SessionPingThread(Glacier2.RouterPrx router, long period)
        {
            _router = router;
            _period = period;
            _done = false;
        }

        public void Run()
        {
            _m.Lock();
            try
            {
                while (!_done)
                {
                    try
                    {
                        _router.begin_refreshSession().whenCompleted(//() => { Console.WriteLine("ping"); },
                                                    (Ice.Exception ex) =>
                                                    {
                                                        this.done();

                                                        if (this.ConnectionLost != null)
                                                        {
                                                            this.ConnectionLost(this, EventArgs.Empty);
                                                        }

                                                    });
                    }
                    catch (Ice.CommunicatorDestroyedException)
                    {
                        //
                        // AMI requests can raise CommunicatorDestroyedException directly.
                        //
                        Console.Error.WriteLine("Catch a CommunicatorDestroyedException.");
                        break;
                    }

                    if (!_done)
                    {
                        _m.TimedWait((int)_period);
                    }
                }
            }
            finally
            {
                _m.Unlock();
            }
        }

        public void done()
        {
            _m.Lock();
            try
            {
                if (!_done)
                {
                    _done = true;
                    _m.NotifyAll();
                }
            }
            finally
            {
                _m.Unlock();
            }
        }
    }
}
