package com.jq.btlib.protocal;

import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;



public interface iStraightFrame {	
	enumProcessResult process(byte[] bBuffer, String uuid) throws FrameFormatIllegalException;
	CsFatScale getFatScale();
}
