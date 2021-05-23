package com.example.job254.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.job254.R;
import com.example.job254.views.adapters.CourseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesFragment extends Fragment {

    ViewGroup mViewGroup;
    private RecyclerView recyclerView;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewGroup = (ViewGroup) inflater.inflate(R.layout.courses, container, false);
        recyclerView = (RecyclerView) mViewGroup.findViewById(R.id.course_recycler);
        
        return mViewGroup;
    }
}
