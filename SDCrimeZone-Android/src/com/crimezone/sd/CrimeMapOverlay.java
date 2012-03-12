package com.crimezone.sd;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CrimeMapOverlay extends ItemizedOverlay<OverlayItem> {

  private static int maxNum = 1000;
  private OverlayItem overlays[] = new OverlayItem[1000];
  private int index = 0;
  private boolean full = false;
  private CrimeMapOverlay itemizedoverlay;

  public CrimeMapOverlay(Drawable defaultMarker) {
    super(boundCenterBottom(defaultMarker));
  }

  @Override
  protected OverlayItem createItem(int i) {
    return overlays[i];
  }

  @Override
  public int size() {
    if (full) {
      return overlays.length;
    } else {
      return index;
    }

  }

  public void addOverlay(OverlayItem overlay) {
    if (index < maxNum) {
      overlays[index] = overlay;
    } else {
      index = 0;
      full = true;
      overlays[index] = overlay;
    }
    index++;
    populate();
  }

}