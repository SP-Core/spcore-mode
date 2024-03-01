package spcore.spnet;

public class AutoResetEvent
{
    private final Object _monitor = new Object();
    private volatile boolean _isOpen = false;

    public AutoResetEvent(boolean open)
    {
        _isOpen = open;
    }

    public boolean waitOne() throws InterruptedException
    {
        synchronized (_monitor) {
            while (!_isOpen) {
                _monitor.wait();
            }
            var result = false;
            if(_isOpen){
                result = true;
            }
            _isOpen = false;
            return result;
        }
    }

    public boolean waitOne(long timeout) throws InterruptedException
    {
        synchronized (_monitor) {
            long t = System.currentTimeMillis();
            while (!_isOpen) {
                _monitor.wait(timeout);
                // Check for timeout
                if (System.currentTimeMillis() - t >= timeout)
                    break;
            }
            var result = false;
            if(_isOpen){
                result = true;
            }
            _isOpen = false;
            return result;
        }
    }

    public void set()
    {
        synchronized (_monitor) {
            _isOpen = true;
            _monitor.notify();
        }
    }

    public void reset()
    {
        _isOpen = false;
    }
}