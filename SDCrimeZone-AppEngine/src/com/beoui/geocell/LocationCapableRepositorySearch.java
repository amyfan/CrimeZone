package com.beoui.geocell;

import java.util.List;

import com.beoui.geocell.model.LocationCapable;

public interface LocationCapableRepositorySearch<T extends LocationCapable> {

	List<T> search(List<String> geocells);
	
}
