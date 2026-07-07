package Spring;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PrototypeDemo {

    //Use to get Prototype scope of an object
    public PrototypeDemo(ObjectProvider<Order> objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Autowired
    private static Order order;

    private final ObjectProvider<Order> objectProvider;

    void process() {
        System.out.println(order.createOrder());
    }

}
//scoped proxy - injects a proxy that resolves the real prototype on each method call
@Component
@Scope(value = "Prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)//NO,DEFAULT,INTERFACE,TARGET_CLASS
class Order {

    public static String createOrder() {
        return "Order Created";
    }
}

/**
 * 1.  Create Scope with ProxyMode: injects a proxy that resolves the real prototype on each method call
 * 2.  Get the object USing ObjectProvider: ObjectProvider defers lookup to call-time
 */

