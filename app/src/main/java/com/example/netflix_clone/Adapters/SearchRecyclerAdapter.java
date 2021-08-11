package com.example.netflix_clone.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix_clone.Model.AllCategory;
import com.example.netflix_clone.Model.CategoryItemList;
import com.example.netflix_clone.R;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> implements Filterable {
    Context context;
    List<AllCategory> allCategoryList;
    List<AllCategory> fullist;

    public SearchRecyclerAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
        this.fullist = new ArrayList<>(allCategoryList);
    }

    public static final class SearchViewHolder extends RecyclerView.ViewHolder{
        RecyclerView itemRecycler;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRecycler=itemView.findViewById(R.id.serach_recycler);
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AllCategory> filteredList= new ArrayList<>();
            if(charSequence==null|| charSequence.length()==0){
                filteredList.addAll(allCategoryList);

            }
            else{
                String filterpattern = charSequence.toString().toLowerCase().trim();
                for(AllCategory item:fullist){
                    if(item.getCategoryTitle().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            allCategoryList.clear();
            allCategoryList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.serachrecyclerlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        setItemRecycler(holder.itemRecycler,allCategoryList.get(position).getCategoryItemList());

    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();

    }
    public void setItemRecycler(RecyclerView recyclerView,List<CategoryItemList> categoryItem){
        ItemRecyclerAdapter itemRecyclerAdapter=new ItemRecyclerAdapter(context,categoryItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);

    }}
