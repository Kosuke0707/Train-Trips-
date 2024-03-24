package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagertabAdapter extends FragmentStateAdapter {
    public ViewPagertabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NotificationsFragment();
            case 1:
                return new AlertsFragment();
            default:
                return null; // Handle unexpected position
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs
    }
}
