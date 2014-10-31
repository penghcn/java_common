
/**
 * 
 * @author penghcn
 * @since  2013-8-19
 */
public class SimplePageBean {
	private int totalCount;//总记录数
	private int totalPages;//总页数，尾页
	private int prevPage;//上一页
	private int nextPage;//下一页
	private int currPage;//当前页
	private int pageSize = 20;
	private boolean isPrev;//
	private boolean isNext;//
	private boolean isEnd;//
	private int offsetStart;//
	private int offsetEnd;//
	
	
	public boolean isNotInit() {
		String check = "0,0,0";
		String wait = totalCount+","+totalPages+","+currPage;
		LogWriter.info("pageBean...check:"+check+"______wait:"+wait);
		if (check.equals(wait))
			return true;
		
		//设置前后页
		setPrevAndNextInit();
		setStartAndEndInit();
		return false;
	}
	public void init(int total) {
		//
		if(pageSize < 1)
			pageSize = 1;
		init(total,pageSize);
	}
	public void init(int total,int pageSize) {
		//
		this.pageSize = pageSize;
		setTotalInit(total);
		setPrevAndNextInit();
		setStartAndEndInit();
	}
	/**
	 * 设置总页数
	 */
	protected void setTotalInit(int total) {
		//
		LogWriter.info("setTotalInit:"+total);
		totalCount = total;
		totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
	}
	/**
	 * 设置上下页
	 */
	protected void setPrevAndNextInit() {
		LogWriter.info("setPrevAndNextInit...");
		currPage = currPage < 1 ? 1 : currPage;
		
		prevPage = currPage - 1;
		nextPage = currPage + 1;
		
		prevPage = prevPage < 1 ? 1 : prevPage;
		nextPage = nextPage < 1 ? 1 : nextPage;
		
		prevPage = prevPage > totalPages ? totalPages : prevPage;
		nextPage = nextPage > totalPages ? totalPages : nextPage;
		
		setEnd(nextPage == totalPages);//是否尾页
		setPrev(prevPage >= 1 && currPage != 1);
		setNext(nextPage <= totalPages && currPage != totalPages);
	}
	/**
	 * 设置分页偏移量
	 */
	protected void setStartAndEndInit() {
		LogWriter.info("setStartAndEndOffsetInit...");
		
		//db2
		offsetStart = (currPage - 1) * pageSize;
		offsetEnd = offsetStart++ + pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isPrev() {
		return isPrev;
	}
	public void setPrev(boolean isPrev) {
		this.isPrev = isPrev;
	}
	public boolean isNext() {
		return isNext;
	}
	public void setNext(boolean isNext) {
		this.isNext = isNext;
	}
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	public int getOffsetStart() {
		return offsetStart;
	}
	public void setOffsetStart(int offsetStart) {
		this.offsetStart = offsetStart;
	}
	public int getOffsetEnd() {
		return offsetEnd;
	}
	public void setOffsetEnd(int offsetEnd) {
		this.offsetEnd = offsetEnd;
	}
	
	
	
	
	
	
	
	
}
