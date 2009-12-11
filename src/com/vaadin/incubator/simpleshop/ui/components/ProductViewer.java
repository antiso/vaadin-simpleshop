package com.vaadin.incubator.simpleshop.ui.components;

import java.math.RoundingMode;
import java.text.NumberFormat;

import com.vaadin.incubator.simpleshop.ShoppingCart;
import com.vaadin.incubator.simpleshop.data.Product;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * This is class defines how a single product should be represented in the item
 * browser.
 * 
 * @author Kim
 * 
 */
public class ProductViewer extends CustomComponent implements ClickListener {

    private static final long serialVersionUID = 9208012559967162736L;

    // The root layout of this component
    private final VerticalLayout mainLayout = new VerticalLayout();

    // The header which is show when the product viewer is collapsed
    private final HorizontalLayout header;

    // Product information labels
    private final Label name;
    private final Label price;
    private final Label description;

    // Action buttons
    private final Button addToCartBtn;
    private final Button expandBtn;

    // The product being show in this component
    private final Product product;

    /**
     * Constructor. Takes as parameter the product being showed in this
     * component.
     * 
     * @param product
     */
    public ProductViewer(Product product) {
        // Se the product
        this.product = product;

        // Set the root layout
        setCompositionRoot(mainLayout);

        // Start building the layout

        // This component should take all the width available
        setWidth("100%");

        // Create the header which is shown when the component is collapsed.
        header = new HorizontalLayout();
        header.setWidth("100%");
        header.setSpacing(true);

        // Create the expander button
        expandBtn = new Button("+", this);
        expandBtn.setWidth(null);
        // Set the button's value according to the expand state
        expandBtn.setValue(false);

        // The expand button is the first component in the header
        header.addComponent(expandBtn);

        // The second component shown in the header should be the product's
        // name. This label should take all the space there is available.
        name = new Label(product.getName());
        header.addComponent(name);
        header.setExpandRatio(name, 1);

        // Create a number formatter for formating the prices.
        NumberFormat nf = NumberFormat.getInstance();
        // Always show exactly two digits
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        // Create the price label with the currency
        price = new Label(nf.format(product.getPrice()) + " EUR");
        // The price label shouldn't take more space than what it needs
        price.setWidth(null);

        // Price is also shown in the header
        header.addComponent(price);

        // The last component in the header is the add button
        addToCartBtn = new Button("Add", this);
        addToCartBtn.setWidth(null);
        header.addComponent(addToCartBtn);

        // Create the description label, but do not attach it to any layout at
        // this point
        description = new Label(product.getDescription());

        // Only add the header to the main layout
        mainLayout.addComponent(header);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        // Check which button has been pressed
        if (event.getButton().equals(expandBtn)) {
            // Expand button was pressed, get the new expand state of the
            // component
            boolean expand = !(Boolean) expandBtn.getValue();
            // Update the state
            expandBtn.setValue(expand);

            // The component should be expanded
            if (expand) {
                // Change the caption of the expand button
                expandBtn.setCaption("-");
                // Add the the description to the main layout, thus expanding
                // the component
                mainLayout.addComponent(description);
            } else {
                // Component should be collapsed, update button label...
                expandBtn.setCaption("+");
                // .. and remove the description from the layout
                mainLayout.removeComponent(description);
            }
        } else if (event.getButton().equals(addToCartBtn)) {
            // Add button was pressed. Add this product to the cart
            ShoppingCart.addProduct(product);
        }
    }
}