package com.thm.unicap.app.connection;

import android.util.Pair;

public interface OnTaskProgressUpdated {
    void onTaskProgressUpdated(Pair<Integer, Integer> progress);
}
