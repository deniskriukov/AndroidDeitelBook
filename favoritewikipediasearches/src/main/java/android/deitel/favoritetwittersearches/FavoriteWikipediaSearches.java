
package android.deitel.favoritetwittersearches;

import android.app.Activity;
import java.util.Arrays;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;


public class FavoriteWikipediaSearches extends Activity {

    private SharedPreferences savedSearches;
    private TableLayout queryTableLayout;
    private EditText queryEditText;
    private EditText tagEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedSearches = getSharedPreferences("searches", MODE_PRIVATE);
        queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
        queryEditText = (EditText) findViewById(R.id.queryEditText);
        tagEditText = (EditText) findViewById(R.id.tagEditText);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);
        Button clearTagsButton = (Button) findViewById(R.id.clearTagsButton);
        clearTagsButton.setOnClickListener(clearTagsButtonListener);
        refreshButtons(null);
    }

    private void refreshButtons(String newTag){
        String[] tags = savedSearches.getAll().keySet().toArray(new String[0]);
        Arrays.sort(tags, String.CASE_INSENSITIVE_ORDER);
        if (newTag != null){
            makeTagGUI(newTag, Arrays.binarySearch(tags, newTag));
        } else {
            for (int index = 0; index < tags.length; ++index){
                makeTagGUI(tags[index], index);
            }
        }
    }

    private void makeTag(String query, String tag){
        String originalQuery = savedSearches.getString(tag, null);
        SharedPreferences.Editor preferencesEditor = savedSearches.edit();
        preferencesEditor.putString(tag, query);
        preferencesEditor.apply();

        if (originalQuery == null){
            refreshButtons(tag);
        }
    }

    private void makeTagGUI(String tag, int index){
        LayoutInflater inflater = (LayoutInflater) getSystemService
                                                   (Context.LAYOUT_INFLATER_SERVICE);
        View newTagView = inflater.inflate(R.layout.tag_new_file, null);

        Button newTagButton = newTagView.findViewById(R.id.newTagButton);
        newTagButton.setText(tag);
        newTagButton.setOnClickListener(queryButtonListener);

        Button newEditButton = (Button) newTagView.findViewById(R.id.newEditButton);
        newEditButton.setOnClickListener(editButtonListener);

        queryTableLayout.addView(newTagView, index);
    }

    private void clearButtons(){
        queryTableLayout.removeAllViews();
    }

    public OnClickListener saveButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (queryEditText.getText().length() > 0 && tagEditText.getText().length() > 0){
                makeTag(queryEditText.getText().toString(), tagEditText.getText().toString());
                queryEditText.setText("");
                tagEditText.setText("");

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(tagEditText.getWindowToken(), 0);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(FavoriteWikipediaSearches.this);
                builder.setTitle(R.string.missingTitle);
                builder.setPositiveButton(R.string.ok, null);
                builder.setMessage(R.string.missingMessage);
                builder.create().show();
            }
        }
    };

    public OnClickListener clearTagsButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(FavoriteWikipediaSearches.this);
            builder.setTitle(R.string.confirmTitle);
            builder.setPositiveButton(R.string.erase, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearButtons();
                    SharedPreferences.Editor preferencesEditor = savedSearches.edit();
                    preferencesEditor.clear();
                    preferencesEditor.apply();
                }
            });
            builder.setCancelable(true);
            builder.setNegativeButton(R.string.cancel, null);
            builder.setMessage(R.string.confirmMessage);
            builder.create().show();
        }
    };

    private final OnClickListener queryButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String buttonText = ((Button)v).getText().toString();
            String query = savedSearches.getString(buttonText, null);
            String urlString = getString(R.string.searchURL) + query;
            Intent getURL = new Intent (Intent.ACTION_VIEW, Uri.parse(urlString));
            startActivity(getURL);
        }
    };

    public OnClickListener editButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow buttonTableRow = (TableRow) v.getParent();
            Button searchButton = (Button) buttonTableRow.findViewById(R.id.newTagButton);
            String tag = searchButton.getText().toString();
            tagEditText.setText(tag);
            queryEditText.setText(savedSearches.getString(tag, null));
        }
    };
}