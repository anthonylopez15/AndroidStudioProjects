package com.exemple.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;

import static android.R.attr.order;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int qtde = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */

    public void acrecent(View view) {
        if (qtde == 100) {
            Toast.makeText(this, "Não é possível add mais de 100 cafés", Toast.LENGTH_SHORT).show();
            return;
        }
        qtde = qtde + 1;
        displayQuanity(qtde);
    }

    public void descrecent(View view) {
        if (qtde == 1) {
            Toast.makeText(this, "Não é possível add menos de 1 café", Toast.LENGTH_SHORT).show();
            return;
        }
        qtde = qtde - 1;
        displayQuanity(qtde);
    }

    public void submitOrders(View view) {
        EditText name = (EditText) findViewById(R.id.your_name_text);
        String yourName = name.getText().toString();

        CheckBox cremeChantilyCheckBox = (CheckBox) findViewById(R.id.creme_chantily_checkbox);
        boolean addChantily = cremeChantilyCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean addChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(addChantily, addChocolate);
        String priceMessage = createOrderSummary(price, addChantily, addChocolate, yourName);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email, yourName));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //nome quantidade total, thank you!!
    public String createOrderSummary(int price, boolean addChantily, boolean addChocolate, String name) {
        String priceOrder = getString(R.string.order_summary_name, name);
        priceOrder += "\n" + getString(R.string.order_summary_cream_chantily, String.valueOf(addChantily));
        priceOrder += "\n " + getString(R.string.order_summary_chocolate, String.valueOf(addChocolate));
        priceOrder += "\n" + getString(R.string.order_summary_quantity, String.valueOf(qtde));
        priceOrder += "\n" + getString(R.string.order_price, NumberFormat.getCurrencyInstance().format(price));
        priceOrder += "\n" + getString(R.string.thank_you);
        return priceOrder;
    }

    public int calculatePrice(boolean addCremeChantily, boolean addChocolate) {
        int basePrice = 5;
        if (addCremeChantily) {
            basePrice = basePrice + 1;
        }
        if (addChocolate) {
            basePrice = basePrice + 2;
        }
        return qtde * basePrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuanity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}