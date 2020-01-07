package com.letsrace;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.letsrace.R;

public class PrefsFragment extends PreferenceFragment {

    private EditTextPreference etxtBtMacAddress;
    private EditTextPreference etxtWifiServerAddress;
    private EditTextPreference etxtPlayerPort;
    private EditTextPreference etxtWifiServerPort;
    private EditTextPreference etxtCenterPosition;
    private EditTextPreference etxtCenterRange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        etxtBtMacAddress = (EditTextPreference)
                getPreferenceScreen().findPreference("pref_MAC_address");
        etxtBtMacAddress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // no input
                if (etxtBtMacAddress.getEditText().getText().toString().equalsIgnoreCase("") == true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input a bluetooth Mac number.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                return true;
            }
        });

        etxtWifiServerAddress = (EditTextPreference)
                getPreferenceScreen().findPreference("pref_WiFiServer_address");
        etxtWifiServerAddress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // no input
                if (etxtWifiServerAddress.getEditText().getText().toString().equalsIgnoreCase("") == true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input the Server address.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                return true;
            }
        });

        etxtPlayerPort = (EditTextPreference)
                getPreferenceScreen().findPreference("pref_WiFiServer_port");
        etxtPlayerPort.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // no input
                if (etxtPlayerPort.getEditText().getText().toString().equalsIgnoreCase("") == true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input a port number.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                if (etxtPlayerPort.getEditText().getText().toString().matches("\\d+(?:\\.\\d+)?") == false) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input a number");
                    builder.setMessage("Please input a port number.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                return true;
            }
        });

        etxtWifiServerPort = (EditTextPreference)
                getPreferenceScreen().findPreference("pref_RaceServer_port");
        etxtWifiServerPort.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // no input
                if (etxtWifiServerPort.getEditText().getText().toString().equalsIgnoreCase("") == true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input a port number.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                if (etxtWifiServerPort.getEditText().getText().toString().matches("\\d+(?:\\.\\d+)?") == false) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input a number");
                    builder.setMessage("Please input a port number.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                return true;
            }
        });

        etxtCenterPosition = (EditTextPreference)
                getPreferenceScreen().findPreference("pref_CenterPosition");
        etxtCenterPosition.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // no input
                if (etxtCenterPosition.getEditText().getText().toString().equalsIgnoreCase("") == true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input a center position. The middle is 0");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                // not an integer
                if (isNumber(etxtCenterPosition.getEditText().getText().toString()) == false) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input an number. (-5 to 5)");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                // less than -5
                if (Integer.parseInt(etxtCenterPosition.getEditText().getText().toString()) < -5) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input an number. (-5 to 5)");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                // more than 5
                if (Integer.parseInt(etxtCenterPosition.getEditText().getText().toString()) > 5) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input an number. (-5 to 5)");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                return true;
            }
        });

        etxtCenterRange = (EditTextPreference)
                getPreferenceScreen().findPreference("pref_CenterRange");
        etxtCenterRange.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // no input
                if (etxtCenterRange.getEditText().getText().toString().equalsIgnoreCase("") == true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Input required");
                    builder.setMessage("Please input a center range.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                // this is decimal, need t change to positive integer only
//                if (etxtCenterRange.getEditText().getText().toString().matches("\\d+(?:\\.\\d+)?") == false) {
//                if (etxtCenterRange.getEditText().getText().toString().matches("^[0-9]*$") == false) {
                if (isNumber(etxtCenterRange.getEditText().getText().toString()) == false) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Number required");
                    builder.setMessage("Please input a number.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                // more than 10
                if (Integer.parseInt(etxtCenterRange.getEditText().getText().toString()) > 10) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Number required");
                    builder.setMessage("Please input a number. (0 - 10)");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                // less than 0
                if (Integer.parseInt(etxtCenterRange.getEditText().getText().toString()) < 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Number required");
                    builder.setMessage("Please input a number. (0 - 10)");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();

                    return false;
                }
                return true;
            }
        });

    }

    boolean isNumber(String string) {
        try {
            int amount = Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}