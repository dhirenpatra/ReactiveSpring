package com.reactiveprogramming.iteminventory.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "items")
public class ItemModel {

    @Id
    private String itemId;

    @Field("item")
    private String itemName;

    @Field("item_price")
    private BigDecimal price;

    @Field("item_desc")
    private String description;

}
