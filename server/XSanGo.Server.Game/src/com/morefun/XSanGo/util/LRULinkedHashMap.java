package com.morefun.XSanGo.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU 算法
 * @author lvmingtao
 *
 * @param <K>
 * @param <V>
 */
public class LRULinkedHashMap<K,V>  extends LinkedHashMap<K,V> {  
    /** */
	private static final long serialVersionUID = -35182014078751336L;
	
	//定义缓存的容量  
    private int capacity;  
    
    //带参数的构造器     
    public LRULinkedHashMap(int capacity){  
    	super(capacity);
        this.capacity = capacity;  
    }  
    
    //实现LRU的关键方法，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素  
    @Override  
    public boolean removeEldestEntry(Map.Entry<K, V> eldest){   
        return size() > capacity;  
    }    
} 
