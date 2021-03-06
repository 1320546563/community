package com.gxh.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean hasPrecious;  //是否有上一页
    private boolean hasNext;  //是否有下一页
    private boolean showFirst;  //是否有首页
    private boolean showEnd;  //是否有尾页
    private Integer page;  //当前页
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();  //页码

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;
        //给page属性赋值
        //展示页码
        pages.add(page);
        for(int i=1;i<4;i++){
            if(page-i > 0){
                pages.add(0,page - i);
            }
            if(page+i <= totalPage){
                pages.add(page + i);
            }
        }
        //是否展示上一页
        if(page == 1){
            hasPrecious=false;
        }else{
            hasPrecious=true;
        }
        //是否展示下一页
        if(page == totalPage){
            hasNext = false;
        }else{
            hasNext = true;
        }
        //是否展示首末页
        if(pages.contains(1)){
            showFirst = false;
        }else{
            showFirst = true;
        }
        if(pages.contains(totalPage)){
            showEnd = false;
        }else{
            showEnd = true;
        }
    }
}
