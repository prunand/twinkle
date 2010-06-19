/*
 * Copyright 2010 Andrew Prunicki
 * 
 * This file is part of Twinkle.
 * 
 * Twinkle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Twinkle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Twinkle.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.prunicki.suzuki.twinkle;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prunicki.suzuki.twinkle.db.SuzukiDAO;

public class Main extends TwinkleActivity {
    public static final String TAG = "SuzukiTwinkle";
    
    private static final String TWINKLE_PKG_NAME = "com.prunicki.twinkle";
    private static final String SUZUKI_TWINKLE_PKG_NAME = "com.prunicki.suzuki.twinkle";
    private static final String ANDROID_SETTINGS_PKG = "com.android.settings";
    private static final String ANDROID_SETTINGS_APP_PKG_NAME = ANDROID_SETTINGS_PKG + ".ApplicationPkgName";
    private static final String INSTALL_APP_DETAILS = ANDROID_SETTINGS_PKG + ".InstalledAppDetails";
    
    private Button mNameButton;
    private Button mPitchButton;
    private Button mRhythmButton;

    private SuzukiApplication app;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPitchButton = (Button) findViewById(R.id.Pitch);
        mRhythmButton = (Button) findViewById(R.id.Rhythm);
        mNameButton = (Button) findViewById(R.id.Name);
        
        mPitchButton.setOnClickListener(mPitchListener);
        mRhythmButton.setOnClickListener(mRhythmListener);
        mNameButton.setOnClickListener(mNameListener);
        
        app = (SuzukiApplication) getApplication();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (twinkleInstalled()) {
            showUninstallDialog();
        } else {
            SuzukiDAO dao = app.getDAO();

            int numDismisses = dao.queryNumberOfDismisses();
            if (numDismisses > 2) {
                numDismisses = 0;
            }

            if (numDismisses == 0) {
                showUpgradeDialog();
            }
            dao.updateNumberOfDismisses(numDismisses + 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                Dialog d = new Dialog(this);
                d.setContentView(R.layout.about);
                d.setCanceledOnTouchOutside(true);
                
                LinearLayout layout = (LinearLayout) d.findViewById(R.id.AboutLayout);
                layout.setOnClickListener(new AboutListener(d));
                View upgradeView = d.findViewById(R.id.Upgrade);
                if (twinkleInstalled()) {
                    upgradeView.setVisibility(View.GONE);
                } else {
                    upgradeView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!startMarket(TWINKLE_PKG_NAME)) {
                                showNoMarketToast();
                            }
                        }
                    });
                }
                
                String appName = getString(R.string.app_name);
                d.setTitle("About " + appName);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUpgradeDialog() {
        AlertDialog.Builder dlgBldr = new AlertDialog.Builder(this);
        dlgBldr.setTitle("Upgrade to Twinkle");
        dlgBldr.setMessage("Suzuki Twinkle is now \"Twinkle\".\n\n" +
            "Would you like to upgrade to Twinkle now?");
        
        dlgBldr.setPositiveButton("Upgrade Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!startMarket(TWINKLE_PKG_NAME)) {
                    showNoMarketToast();
                }
                finish();
            }
        });
        dlgBldr.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        AlertDialog dlg = dlgBldr.create();
        dlg.show();
    }
    
    private void showUninstallDialog() {
        AlertDialog.Builder dlgBldr = new AlertDialog.Builder(this);
        dlgBldr.setTitle("Uninstall Suzuki Twinkle");
        dlgBldr.setMessage("Twinkle has replaced Suzuki Twinkle and you have it.\n\n" +
            "Would you like to uninstall Suzuki Twinkle now?");
        
        dlgBldr.setPositiveButton("Uninstall Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!startMarket(SUZUKI_TWINKLE_PKG_NAME)) {
                    startManageAppActivity();
                }

                finish();
            }
        });
        dlgBldr.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        AlertDialog dlg = dlgBldr.create();
        dlg.show();
    }
    
    private boolean twinkleInstalled() {
        Intent twinkleIntent = new Intent();
        twinkleIntent.setClassName(TWINKLE_PKG_NAME, TWINKLE_PKG_NAME + ".Main");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> intentActivities = pm.queryIntentActivities(twinkleIntent, PackageManager.MATCH_DEFAULT_ONLY);
        int size = intentActivities.size();
        return size > 0;
    }
    
    void startManageAppActivity() {
        Intent intent = new Intent();
        intent.setClassName(ANDROID_SETTINGS_PKG, INSTALL_APP_DETAILS);
        intent.putExtra(ANDROID_SETTINGS_APP_PKG_NAME, SUZUKI_TWINKLE_PKG_NAME);
        startActivity(intent);
    }
    
    boolean startMarket(String pkgName) {
        boolean started = true;
        Uri marketUri = Uri.parse("market://search?q=pname:" + pkgName);
        Intent marketIntent =  new Intent(Intent.ACTION_VIEW).setData(marketUri);
        try {
            startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            started = false;
        }
        return started;
    }

    void showNoMarketToast() {
        Toast toast = Toast.makeText(Main.this, "Android Market not found!", Toast.LENGTH_LONG);
        toast.show();
    }

    private OnClickListener mPitchListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(Main.this, Pitch.class);
            
            Main.this.startActivity(intent);
        }
    };

    private OnClickListener mRhythmListener = new OnClickListener() {
        public void onClick(View v) {
            SightSoundDialog dialog = new SightSoundDialog(Main.this, SeeRhythm.class, HearRhythm.class);
            dialog.show();
        }
    };

    private OnClickListener mNameListener = new OnClickListener() {
        public void onClick(View v) {
            SightSoundDialog dialog = new SightSoundDialog(Main.this, SeeNote.class, HearNote.class);
            dialog.show();
        }
    };

    private class AboutListener implements OnClickListener {
        private Dialog mDialog;
        
        public AboutListener(Dialog dialog) {
            mDialog = dialog;
        }
        
        public void onClick(View v) {
            mDialog.cancel();
        }
    };
}