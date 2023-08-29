package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;

public class ListResultService<E> {

   private int totalRecords;
   private List<E> list;

public ListResultService(int page,int maxResult, Query<E> query) {
      final int pageIndex = page - 1 < 0 ? 0 : page - 1;

      int fromRecordIndex = pageIndex * maxResult;
      int maxRecordIndex = fromRecordIndex + maxResult;

      ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);

      List<E> results = new ArrayList<>();

      boolean hasResult = resultScroll.first();

      if (hasResult) {
         // Scroll to position:
         hasResult = resultScroll.scroll(fromRecordIndex);

         if (hasResult) {
            do {
               @SuppressWarnings("unchecked")
			E record = (E)resultScroll.get(0);
               results.add(record);
            } while (resultScroll.next()//
                  && resultScroll.getRowNumber() >= fromRecordIndex
                  && resultScroll.getRowNumber() < maxRecordIndex);

         }

         // Go to Last record.
         resultScroll.last();
      }
      this.list = results;
      this.totalRecords = resultScroll.getRowNumber() + 1;
   }


   public int getTotalRecords() {
      return totalRecords;
   }
   
   public List<E> getList() {
      return list;
   }

}