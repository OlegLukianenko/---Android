package com.itstep.oleg.myanimation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


class MyMovie
{
    public String title;
    public String genre;
    public  int year;

    public MyMovie (String title, String genre, int year)
    {
        this.title=title;
        this.genre=genre;
        this.year=year;
    }
}

public class MainActivity extends AppCompatActivity
{
    private ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setSubtitle("Movies List");

        toolbar.setNavigationIcon(R.mipmap.ic_add_circle_outline_white_36dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("######", "Navigation Icon Click");
            }
        });

        this.lvMovies = (ListView) this.findViewById(R.id.lvMovies);


        String[] genres = {
                "Action", "Fantastic", "Drama", "Melodrama",
                "Comedy", "Adventure", "Cartoon", "Thriller", "LoveStory"
        };

        ArrayList<MyMovie> movies = new ArrayList<>();

        for (int i=0; i<50;i++)
        {
            movies.add(new MyMovie(
                    "Movie Hello World " + (i+1),
                    genres[(int)(Math.random() * genres.length)],
                    2000+ (int)(Math.random() * 17)));
        }


        ArrayAdapter<MyMovie> adapter =
                new ArrayAdapter<MyMovie>(this, R.layout.list_item_value_animation, R.id.tvTitle, movies)
        {
            @Override
                    public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                MyMovie F = this.getItem(position);


                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                TextView tvGenre = (TextView) view.findViewById(R.id.tvGenre);
                TextView tvYear = (TextView) view.findViewById(R.id.tvYear);

                tvTitle.setText(F.title);
                tvGenre.setText(F.genre);
                tvYear.setText(String.valueOf(F.year));

                return view;
            }
        };

        this.lvMovies.setAdapter(adapter);

        this.lvMovies.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final FrameLayout flAddPanel = (FrameLayout) findViewById(R.id.flAddPanel);
                        MainActivity.this.toggleValueAnimation(view);
                        if (flAddPanel.getHeight() >= 109) {
                            frameValueAnimation((View) view.getParent().getParent());
                        }
                    }
                });

    }

    private   void toggleValueAnimation(View view)
    {
        final LinearLayout llButtonHolder = (LinearLayout) view.findViewById(R.id.llButtonHolder);
        final LinearLayout llItemHolder = (LinearLayout) view.findViewById(R.id.llItemHolder);

        final TextView tvDelete = (TextView) view.findViewById(R.id.tvDelete);
        final TextView tvEdit = (TextView) view.findViewById(R.id.tvEdit);

        AnimatorSet asRise;

        if(llButtonHolder.getWidth()==0)
        {
            //показать
            asRise = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                    R.animator.value_animation_item_rise);
        }
        else
        {
            //спрятать
            asRise = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                    R.animator.value_animation_item_fall);
        }

        asRise.setInterpolator(new AccelerateDecelerateInterpolator());

        ArrayList<Animator> arrL = asRise.getChildAnimations();

        ValueAnimator vaRise = (ValueAnimator)arrL.get(0);
        vaRise.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curWidth = (int) animation.getAnimatedValue();

                FrameLayout.LayoutParams LP = new
                        FrameLayout.LayoutParams(curWidth,
                        FrameLayout.LayoutParams.MATCH_PARENT);
                LP.gravity = Gravity.RIGHT;
                llButtonHolder.setLayoutParams(LP);
            }
        });


        ValueAnimator vaRiseTextSize = (ValueAnimator)arrL.get(1);
        vaRiseTextSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float curTextSize = (float) animation.getAnimatedValue();
                tvDelete.setTextSize(TypedValue.COMPLEX_UNIT_SP, curTextSize);
                tvEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, curTextSize);
            }
        });


        ValueAnimator vaRiseShift = (ValueAnimator)arrL.get(2);
        vaRiseShift.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curX = (int) animation.getAnimatedValue();
                llItemHolder.setX(curX);
            }
        });

        asRise.start();

    }

    public void txtClick(View view)
    {
        switch (view.getId()) {
            case R.id.tvEdit: {

                toggleValueAnimation((View) view.getParent().getParent());
                view.getId();
                Log.d("-----------------------", "   -----------------" + view.getId());

                final FrameLayout flAddPanel = (FrameLayout) this.findViewById(R.id.flAddPanel);
                final LinearLayout llEditHolder = (LinearLayout) this.findViewById(R.id.llEditHolder);


                AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                        R.animator.value_animation_frame_size_big);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                ArrayList<Animator> arrL = anim.getChildAnimations();
                ValueAnimator vaRise = (ValueAnimator) arrL.get(0);

                vaRise.addUpdateListener(
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                flAddPanel.getLayoutParams().height = (int) animation.getAnimatedValue();
                                flAddPanel.requestLayout();
                            }
                        });

                ValueAnimator vaRiseTextSize = (ValueAnimator) arrL.get(1);
                vaRiseTextSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float curTextSize = (float) animation.getAnimatedValue();
                        llEditHolder.setAlpha(curTextSize);

                    }
                });

                anim.start();
            }
            break;
        }
    }

    public void btnClick(View view)
    {
        frameValueAnimation((View) view.getParent().getParent());
    }

    private   void frameValueAnimation(View view)
    {
        final FrameLayout flAddPanel = (FrameLayout)this.findViewById(R.id.flAddPanel);
        final LinearLayout llEditHolder = (LinearLayout)this.findViewById(R.id.llEditHolder);


        AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.value_animation_frame_size_small);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> arrL = anim.getChildAnimations();

        ValueAnimator vaRiseTextSize = (ValueAnimator)arrL.get(1);
        vaRiseTextSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float curTextSize = (float) animation.getAnimatedValue();
                llEditHolder.setAlpha(curTextSize);

            }
        });

        ValueAnimator vaRise = (ValueAnimator)arrL.get(0);

        vaRise.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        flAddPanel.getLayoutParams().height = (int) animation.getAnimatedValue();
                        flAddPanel.requestLayout();
                    }
                });


        anim.start();

    }


}









































