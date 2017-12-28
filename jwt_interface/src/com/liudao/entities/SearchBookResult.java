package com.liudao.entities;

import java.util.List;
/**
 * 返回用户的查询书籍结果实例
 * @author liudao
 *
 */
public class SearchBookResult {
	/**
	 * 查询的符合条件的书籍数量
	 */
	private int count;
	/**
	 *  符合查询条件的书籍集合
	 */
	private List<Book> books;
	public void setCount(int count) {
		this.count = count;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public int getCount() {
		return count;
	}
	public List<Book> getBooks() {
		return books;
	}
	@Override
	public String toString() {
		return "SearchBookResult [count=" + count + ", books=" + books + "]";
	}
}
