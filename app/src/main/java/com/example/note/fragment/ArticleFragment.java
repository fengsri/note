package com.example.note.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.note.GoodActivity;
import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.adapter.ImagerViewPager;
import com.example.note.adapter.MyViewPagerAdapter;
import com.example.note.view.CircleIndicator;
import com.example.note.view.RecyclerScrollView;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment implements View.OnClickListener{

    private TextView articleText1;
    private TextView articleText2;
    private TextView articleText3;
    private TextView articleText4;
    private TextView articleText5;
    private TextView articleText12;
    private TextView articleText22;
    private TextView articleText32;
    private TextView articleText42;
    private TextView articleText52;
    private TextView articleMove;//移动
    private TextView articleMove2;//移动
    private Context context;
    private ViewPager headerViewPager;
    private ViewPager contentViewPager;
    private CircleIndicator header_indicator; //header导航圆

    private RelativeLayout header;
    private RecyclerScrollView scrollView;
    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;

    private List<ImageView> imageViewListHeader=new ArrayList<ImageView>(); //头部轮播图
    private List<Fragment> fragmentList=new ArrayList<Fragment>();//主体内容
    private int mCurrent=0; //当前的viewpager的哪一页
    private float distance=0; //当前的距离
    private float screenWidth=0;//当前的屏幕宽度


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view =LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_article,container,false);
        ((MainActivity)context).hinden(2);
        initData();
        init(view);
        return view;
    }

    public void init(View view){
        getscreenwidth();
        relativeLayout1 = view.findViewById(R.id.article_dh);
        relativeLayout2 = view.findViewById(R.id.article_dh2);
        scrollView = view.findViewById(R.id.my_scrollView);
        header = view.findViewById(R.id.article_rheader);
        headerViewPager= view.findViewById(R.id.article_header);
        contentViewPager = view.findViewById(R.id.article_content);
        articleMove = view.findViewById(R.id.article_daohang_move);
        articleText1 = view.findViewById(R.id.article_text1);
        articleText2 = view.findViewById(R.id.article_text2);
        articleText3 = view.findViewById(R.id.article_text3);
        articleText4 = view.findViewById(R.id.article_text4);
        articleText5 = view.findViewById(R.id.article_text5);
        articleMove2 = view.findViewById(R.id.article_daohang_move2);
        articleText12 = view.findViewById(R.id.article_text12);
        articleText22 = view.findViewById(R.id.article_text22);
        articleText32 = view.findViewById(R.id.article_text32);
        articleText42 = view.findViewById(R.id.article_text42);
        articleText52 = view.findViewById(R.id.article_text52);
        header_indicator = view.findViewById(R.id.article_header_indicator);
        //设置头部轮播图
        ImagerViewPager imagerViewPager=new ImagerViewPager(imageViewListHeader);
        headerViewPager.setAdapter(imagerViewPager);
        header_indicator.setUpWithViewPager(headerViewPager);

        //设置主体内容viewpager
        MyViewPagerAdapter viewPagerAdapter=new MyViewPagerAdapter(this.getChildFragmentManager(),fragmentList);
        contentViewPager.setAdapter(viewPagerAdapter);
        contentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                move(distance,distance+((screenWidth/5)*(position-mCurrent)),position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        scrollView.setOnScrollListener(new RecyclerScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY >=(header.getHeight()+35)){
                    relativeLayout1.setVisibility(View.INVISIBLE);
                    relativeLayout2.setVisibility(View.VISIBLE);
                }else {
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout2.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void initData(){
        final int imageId[]={R.drawable.luncher_bg1,R.drawable.luncher_bg2,R.drawable.luncher_bg3,R.drawable.luncher_bg5,R.drawable.luncher_bg2};
        for(int i=0;i<5;i++) {//循环5次加载轮播图，并设置轮播图的点击事件
            final ImageView imageView=new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            Glide.with(context)
//                    .load(imageId[i])
//                    .error(R.drawable.practice_header22)
//                    .dontAnimate()
//                    .into(imageView);
            imageView.setImageResource(imageId[i]);
            imageView.setTag(i);
            imageViewListHeader.add(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, GoodActivity.class);
                    intent.putExtra("tag",(int)imageView.getTag());
                    context.startActivity(intent);
                }
            });
        }

        //主体内容
        fragmentList.add(new ArticleItem1Fragment());
        fragmentList.add(new ArticleItem2Fragment());
        fragmentList.add(new ArticleItem3Fragment());
        fragmentList.add(new ArticleItem4Fragment());
        fragmentList.add(new ArticleItem5Fragment());
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }


    //获取屏幕的宽度
    private void getscreenwidth(){
        WindowManager wm = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth= wm.getDefaultDisplay().getWidth();
    }

    //用于导航移动
    @SuppressLint("ObjectAnimatorBinding")
    private void move(float tx, float t2x, int p){
        ObjectAnimator.ofFloat(articleMove,"translationX",tx,t2x).setDuration(100).start();
        ObjectAnimator.ofFloat(articleMove2,"translationX",tx,t2x).setDuration(100).start();
        distance=t2x;
        mCurrent=p;
        setColor(p);
        System.out.print(p);
    }
    //设置颜色
    public void setColor(int i) {
        articleText1.setTextColor(Color.parseColor("#2b2b2b"));
        articleText2.setTextColor(Color.parseColor("#2b2b2b"));
        articleText3.setTextColor(Color.parseColor("#2b2b2b"));
        articleText4.setTextColor(Color.parseColor("#2b2b2b"));
        articleText5.setTextColor(Color.parseColor("#2b2b2b"));
        articleText12.setTextColor(Color.parseColor("#2b2b2b"));
        articleText22.setTextColor(Color.parseColor("#2b2b2b"));
        articleText32.setTextColor(Color.parseColor("#2b2b2b"));
        articleText42.setTextColor(Color.parseColor("#2b2b2b"));
        articleText52.setTextColor(Color.parseColor("#2b2b2b"));
        switch (i){
            case 0:{
                articleText1.setTextColor(Color.parseColor("#5299f5"));
                articleText12.setTextColor(Color.parseColor("#5299f5"));
                break;
            }
            case 1:{
                articleText2.setTextColor(Color.parseColor("#5299f5"));
                articleText22.setTextColor(Color.parseColor("#5299f5"));
                break;
            }
            case 2:{
                articleText3.setTextColor(Color.parseColor("#5299f5"));
                articleText32.setTextColor(Color.parseColor("#5299f5"));
                break;
            }
            case 3:{
                articleText4.setTextColor(Color.parseColor("#5299f5"));
                articleText42.setTextColor(Color.parseColor("#5299f5"));
                break;
            }
            case 4:{
                articleText5.setTextColor(Color.parseColor("#5299f5"));
                articleText52.setTextColor(Color.parseColor("#5299f5"));
                break;
            }
            default:
                break;
        }
    }

}

