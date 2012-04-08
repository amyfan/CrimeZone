package com.crimezone.sd;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CrimeMapOverlay extends ItemizedOverlay<OverlayItem> {

  private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
  private Context mContext;

  public CrimeMapOverlay(Drawable defaultMarker, Context context) {
    super(boundCenterBottom(defaultMarker));
    mContext = context;
  }

  @Override
  protected OverlayItem createItem(int i) {
    return overlays.get(i);
  }

  @Override
  public int size() {
    return overlays.size();
  }

  @Override
  protected boolean onTap(int index) {
    if (overlays.size() > 0) {
      OverlayItem item = overlays.get(index);

      // Do stuff here when you tap, i.e. :
      AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
      dialog.setTitle(item.getTitle());
      dialog.setMessage(item.getSnippet());
      AlertDialog mDialog = dialog.create();
      mDialog.setCanceledOnTouchOutside(true);
      mDialog.show();
    }

    // return true to indicate we've taken care of it
    return true;
  }

  public void addOverlay(OverlayItem overlay) {
    overlays.add(overlay);
    populate();
  }

}