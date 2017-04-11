package com.example.android.justjava;

/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee. Test
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method increases the quantity value on the screen.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You can't order more than 100 cups", Toast.LENGTH_SHORT).show();
            return;
        } else {
            quantity = quantity + 1;
            displayQuantity(quantity);
        }
    }

    /**
     * This method decreases the quantity value on the screen.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You can't order less than 1 cup", Toast.LENGTH_SHORT).show();
            return;
        } else {
            quantity = quantity - 1;
            displayQuantity(quantity);
        }
    }

    /**
     * This method is called when the order button is clicked.
     *
     * @param view
     */
    public void submitOrder(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString();
        Log.v("MainActivity", "Name typed is: " + name);

        CheckBox whippedCreamBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        Boolean hasWhippedCream = whippedCreamBox.isChecked();
        Log.v("MainActivity", "Has Whipped Cream: " + hasWhippedCream);

        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        Boolean hasChocolate = chocolate.isChecked();
        Log.v("MainActivity", "Has Chocolate: " + hasChocolate);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String message = createOrderSummary(price, name, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "mscmazi@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     *
     * @param number gets the number from the actual variable quantity
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Method that calculates the price
     *
     * @param hasWhippedCream: if true, add 1$
     * @param hasChocolate:    if true, add 2$
     * @return the total cost
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int price = 5;
        if ((hasWhippedCream) && (hasChocolate)) {
            price += 3;
        } else if (hasChocolate) {
            price += 2;
        } else if (hasWhippedCream) {
            price += 1;
        }
        return quantity * price;
    }


    /**
     * This method creates a summary of all the details of the order
     *
     * @param price:          gets the price by calculatePrice()
     * @param name            passes the name to the output message
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate    is whether or not the user wants chocolate
     * @return a string with the summary of the order
     */
    private String createOrderSummary(int price, String name, boolean hasWhippedCream, boolean hasChocolate) {
        return getString(R.string.order_summary_name, name)
                + "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream)
                + "\n" + getString(R.string.order_summary_chocolate, hasChocolate)
                + "\n" + getString(R.string.order_summary_quantity, quantity)
                + "\n" + getString(R.string.order_summary_price, Integer.toString(price))
                + "\n" + getString(R.string.thank_you);
    }
}