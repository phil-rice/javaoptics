package one.xingyi.profile;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@Setter
@Getter
public class StartProfileTag extends TagSupport {

    private String id; // An identifier for the profile segment
    private IProfile profiler;


    @Override
    public int doStartTag() throws JspException {
        long startTime = profiler.timer().nanoTime();
        pageContext.setAttribute("profileStartTime_" + id, startTime); // Store start time in pageContext
        return SKIP_BODY;
    }
}
