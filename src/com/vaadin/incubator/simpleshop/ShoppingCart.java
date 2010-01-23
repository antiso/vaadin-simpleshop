package com.vaadin.incubator.simpleshop;

import com.vaadin.incubator.simpleshop.data.Order;
import com.vaadin.incubator.simpleshop.data.Product;
import com.vaadin.incubator.simpleshop.data.User;
import com.vaadin.incubator.simpleshop.events.CartUpdatedEvent;
import com.vaadin.incubator.simpleshop.events.CartUpdatedEvent.EventType;

/**
 * A static class which handles the cart content within the entire application.
 * 
 * @author Kim
 * 
 */
public class ShoppingCart {

    // Use the thread local pattern to have an application specific cart content
    // which can be accessed in a static manner.
    private static ThreadLocal<Order> currentOrder = new ThreadLocal<Order>();

    /**
     * Get the order object for this shopping cart
     * 
     * @return
     */
    public static Order getOrder() {
        return currentOrder.get();
    }

    /**
     * Set the order object for this shopping cart
     * 
     * @param order
     */
    public static void setOrder(Order order) {
        currentOrder.set(order);
    }

    /**
     * Add a new product to the cart content. If this product already exists in
     * the cart, then increment the quantity by one.
     * 
     * @param product
     */
    public static void addProduct(Product product) {
        getOrder().addProduct(product);
        CartUpdatedEvent event = new CartUpdatedEvent(EventType.PRODUCT_ADDED,
                product);
        SimpleshopApplication.getEventHandler().dispatchEvent(event);
    }

    /**
     * Remove the given product from the cart content
     * 
     * @param product
     */
    public static void removeProduct(Product product) {
        getOrder().removeProduct(product);
        CartUpdatedEvent event = new CartUpdatedEvent(
                EventType.PRODUCT_REMOVED, product);
        SimpleshopApplication.getEventHandler().dispatchEvent(event);
    }

    /**
     * Modify the ordered quantity for the given product. If quantity is zero or
     * less than zero, then the product will be removed from the cart entirely.
     * 
     * @param product
     * @param quantity
     */
    public static void setQuantity(Product product, int quantity) {
        if (quantity <= 0) {
            removeProduct(product);
            CartUpdatedEvent event = new CartUpdatedEvent(
                    EventType.PRODUCT_REMOVED, product);
            SimpleshopApplication.getEventHandler().dispatchEvent(event);
        } else {
            getOrder().setQuantity(product, quantity);
            CartUpdatedEvent event = new CartUpdatedEvent(
                    EventType.PRODUCT_QUANTITY_CHANGED, product);
            SimpleshopApplication.getEventHandler().dispatchEvent(event);
        }
    }

    /**
     * Prefills the order's contact information fields with the given user's
     * details.
     * 
     * @param user
     */
    public static void prefillContactInformation(User user) {
        Order order = getOrder();
        if (user != null) {
            // Check if the name is set in the order. If not, then copy the name
            // from the user object.
            if (order.getName() == null || order.getName().isEmpty()) {
                order.setName(user.getName());
            }
            // Check if the street name is set in the order. If not, then copy
            // the street name from the user object.
            if (order.getStreetName() == null
                    || order.getStreetName().isEmpty()) {
                order.setStreetName(user.getStreetName());
            }

            // Check if the zip is set in the order. If not, then copy
            // the zip from the user object.
            if (order.getZip() == null || order.getZip().isEmpty()) {
                order.setZip(user.getZip());
            }

            // Check if the city is set in the order. If not, then copy
            // the city from the user object.
            if (order.getCity() == null || order.getCity().isEmpty()) {
                order.setCity(user.getCity());
            }

            // Check if the email is set in the order. If not, then copy
            // the email from the user object.
            if (order.getEmail() == null || order.getEmail().isEmpty()) {
                order.setEmail(user.getEmail());
            }
        }
    }

    /**
     * Clear all contact information details from the current order.
     */
    public static void clearContactInfo() {
        // Clear all the contact information details from the order.
        Order order = getOrder();
        order.setName(null);
        order.setStreetName(null);
        order.setZip(null);
        order.setCity(null);
        order.setPhone(null);
        order.setEmail(null);
        order.setComments(null);
    }

}
