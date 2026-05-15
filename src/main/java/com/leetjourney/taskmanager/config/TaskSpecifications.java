package com.leetjourney.taskmanager.config;


import com.leetjourney.taskmanager.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> hasTitle (String title){
        return (root , query , cb) ->
                title == null ? null :
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase()+ "%");
    }

    public static Specification<Task> hasCategoryName (String categoryName){
        return (root , query , cb) ->
                categoryName == null || categoryName.isBlank() ? null :
                        cb.like(cb.lower(root.get("category").get("name")), "%" + categoryName.toLowerCase()+ "%");
    }

    public static Specification<Task> hasStatus (Boolean taskStatus){
        return (root , query , cb) ->
                taskStatus == null ? null :
                        cb.equal(root.get("taskStatus"), taskStatus);
    }

    // Combine all filters
    public static Specification<Task> withFilters(String title, String categoryName, Boolean taskStatus) {
        return Specification.allOf(
                hasTitle(title),
                hasCategoryName(categoryName),
                hasStatus(taskStatus));
    }

}
