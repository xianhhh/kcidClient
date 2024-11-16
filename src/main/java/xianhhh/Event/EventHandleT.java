package xianhhh.Event;

import com.google.common.eventbus.EventBus;
import xianhhh.Exception.EventException;

import java.util.HashMap;

public class EventHandleT {
    //private EventBus bus;

    private final EventBus bus;

    public EventHandleT() {
        bus = new EventBus();
        //bus = new AsyncEventBus(Executors.newCachedThreadPool());
    }

    public final HashMap<Object, Boolean> zcv = new HashMap<Object, Boolean>();

    private final HashMap<Object, Boolean> eventFire = new HashMap<Object, Boolean>();

    private void post(Event e) {
        bus.post(e);
    }

    private void register(Object obj) {
        zcv.put(obj, true);
        bus.register(obj);
    }





    private void unregister(Object obj) {
        zcv.put(obj, false);
        bus.unregister(obj);
    }
    public void fire(Object e) {
        eventFire.put(e, true);
    }

    public void unfire(Object e) {
        eventFire.put(e, false);
    }

    public void supe(Object obj,Mode mode) {
    try {
        switch (mode) {
            case POST:
                if (obj instanceof Event) {
                    Event v = (Event) obj;
                    post(v);
                } else {
                    throw new EventException("Not true event");
                }
                break;

            case REGISTER:
                register(obj);
                break;
            case UNREGISTER:
                unregister(obj);
                break;
            default:
                throw new RegisterException("NULL");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }


    public void supev(Object obj, Mode m) {
        try {
            switch (m) {
                case POST:
                    if (obj instanceof Event) {
                        Event v = (Event) obj;

                        if (eventFire.get(v) == null) {
                            unfire(v);
                        }

                        if (!eventFire.get(v)) {
                            post(v);
                        }
                    } else {
                        throw new EventException("Not true event");
                    }
                    break;

                case REGISTER:
                    zcv.putIfAbsent(obj, false);
                    if (!zcv.get(obj)) {
                        register(obj);
                    } else {
                        throw new RegisterException("A registered object");
                    }
                    break;
                case UNREGISTER:
                    zcv.putIfAbsent(obj, true);
                    if (zcv.get(obj)) {
                        unregister(obj);
                    } else {
                        throw new RegisterException("Did you register this");
                    }
                    break;

                default:
                    throw new RegisterException("NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void supe(Object obj, Mode m, boolean debug) {
        Object lock = new Object();
        try {
            switch (m) {
                case POST:
                    if (obj instanceof Event) {
                        Event v = (Event) obj;
                        synchronized (lock) {
                            if (eventFire.get(v) == null && debug) {
                                unfire(v);
                                System.out.println("unfire " + v.getName());
                            } else {
                                unfire(v);
                            }

                            if (!eventFire.get(v) && debug) {
                                post(v);
                                System.out.println("post: " + v.getName());
                            } else {
                                post(v);
                            }
                        }
                    } else {
                        throw new EventException("Not true event");
                    }
                    break;

                case REGISTER:
                    zcv.putIfAbsent(obj, false);
                    if (!zcv.get(obj)) {
                        register(obj);
                    } else {
                        throw new RegisterException("A registered object");
                    }
                    break;
                case UNREGISTER:
                    zcv.putIfAbsent(obj, true);
                    if (zcv.get(obj)) {
                        unregister(obj);
                    } else {
                        throw new RegisterException("Did you register this");
                    }
                    break;

                default:
                    throw new RegisterException("NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public enum Mode {
        POST,
        REGISTER,
        UNREGISTER
    }

    private class RegisterException extends Exception {
        public RegisterException(String s) {
            super(s);
        }

        public RegisterException() {
            super();
        }
    }
}
