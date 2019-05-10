package com.example.note.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.note.GoodActivity;
import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.adapter.ImagerViewPager;
import com.example.note.adapter.MyViewPagerAdapter;
import com.example.note.util.ImageUtil;
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0) {
                int pager = headerViewPager.getCurrentItem();
                headerViewPager.setCurrentItem(pager+1);
            }
        }
    };
    private boolean isLoop = true;

    private PullRefreshLayout pullRefreshLayout;
    private ArticleItem1Fragment fragment1;
    private ArticleItem2Fragment fragment2;
    private ArticleItem3Fragment fragment3;
    private ArticleItem4Fragment fragment4;
    private ArticleItem5Fragment fragment5;

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
        pullRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        relativeLayout1 = view.findViewById(R.id.article_dh);
        relativeLayout2 = view.findViewById(R.id.article_dh2);
        scrollView = view.findViewById(R.id.my_scrollView);
        header = view.findViewById(R.id.article_rheader);
        headerViewPager= view.findViewById(R.id.article_header);
        contentViewPager = view.findViewById(R.id.article_content);
        articleMove = view.findViewById(R.id.article_daohang_move);
        articleText1 = view.findViewById(R.id.article_text1);
        articleText1.setOnClickListener(this);
        articleText2 = view.findViewById(R.id.article_text2);
        articleText2.setOnClickListener(this);
        articleText3 = view.findViewById(R.id.article_text3);
        articleText3.setOnClickListener(this);
        articleText4 = view.findViewById(R.id.article_text4);
        articleText4.setOnClickListener(this);
        articleText5 = view.findViewById(R.id.article_text5);
        articleText5.setOnClickListener(this);
        articleMove2 = view.findViewById(R.id.article_daohang_move2);
        articleText12 = view.findViewById(R.id.article_text12);
        articleText12.setOnClickListener(this);
        articleText22 = view.findViewById(R.id.article_text22);
        articleText22.setOnClickListener(this);
        articleText32 = view.findViewById(R.id.article_text32);
        articleText32.setOnClickListener(this);
        articleText42 = view.findViewById(R.id.article_text42);
        articleText42.setOnClickListener(this);
        articleText52 = view.findViewById(R.id.article_text52);
        articleText52.setOnClickListener(this);
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
        initAuto();
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP );
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shuaxing();
                        // 刷新3秒完成
                        pullRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }


    public void shuaxing(){
        if(mCurrent==0){
            fragment1.initData();
            fragment1.adapter.notifyDataSetChanged();

        }else if(mCurrent==1){
            fragment2.initData();
            fragment2.adapter.notifyDataSetChanged();

        }else if(mCurrent==2){
            fragment3.initData();
            fragment3.adapter.notifyDataSetChanged();

        }else if(mCurrent==3){
            fragment4.initData();
            fragment4.adapter.notifyDataSetChanged();

        }else if(mCurrent==4){
            fragment5.initData();
            fragment5.adapter.notifyDataSetChanged();

        }
    }

    public void initData(){
        for(int i=0;i<150;i++) {//循环5次加载轮播图，并设置轮播图的点击事件
            final ImageView imageView=new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(ImageUtil.getRandImageId());
            imageView.setTag(i);
            imageViewListHeader.add(imageView);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, GoodActivity.class);
                    intent.putExtra("tag", finalI);
                    context.startActivity(intent);
                }
            });
        }
        fragment1 = new ArticleItem1Fragment();
        fragment2 = new ArticleItem2Fragment();
        fragment3 = new ArticleItem3Fragment();
        fragment4 = new ArticleItem4Fragment();
        fragment5 = new ArticleItem5Fragment();
        //主体内容
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
        fragmentList.add(fragment5);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.article_text1:{
                contentViewPager.setCurrentItem(0);
                break;
            }
            case R.id.article_text2:{
                contentViewPager.setCurrentItem(1);
                break;
            }
            case R.id.article_text3:{
                contentViewPager.setCurrentItem(2);
                break;
            }
            case R.id.article_text4:{
                contentViewPager.setCurrentItem(3);
                break;
            }
            case R.id.article_text5:{
                contentViewPager.setCurrentItem(4);
                break;
            }
            case R.id.article_text12:{
                contentViewPager.setCurrentItem(0);
                break;
            }
            case R.id.article_text22:{
                contentViewPager.setCurrentItem(1);
                break;
            }
            case R.id.article_text32:{
                contentViewPager.setCurrentItem(2);
                break;
            }
            case R.id.article_text42:{
                contentViewPager.setCurrentItem(3);
                break;
            }
            case R.id.article_text52:{
                contentViewPager.setCurrentItem(4);
                break;
            }
            default:
                break;
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


    //自动滑动
    private void initAuto(){
        // 自动切换页面功能
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isLoop) {
                    SystemClock.sleep(2000);
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

}

