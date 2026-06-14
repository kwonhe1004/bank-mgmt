package khe.banking.util;

// Represents a view stored in navigation history.

public record ViewState(ViewData<?> viewData, ViewType viewType) {

}

//public class ViewStateC {
//	private final ViewData<?> viewData;
//    private final String viewId;
//    
//    public ViewStateC(ViewData<?> viewData, String viewId) {
//    	this.viewData = viewData;
//        this.viewId = viewId;
//    }
//    
//    public ViewData<?> getViewData() {
//        return viewData;
//    }
//    
//    public String getViewId() {
//        return viewId;
//    }
//}