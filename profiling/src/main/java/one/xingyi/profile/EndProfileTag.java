package one.xingyi.profile;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@Setter
@Getter
public class EndProfileTag extends TagSupport {

    private String id;
    private IProfile profiler;

    @Override
    public int doStartTag() throws JspException {
        long endTime = profiler.timer().nanoTime();
        long startTime = (long) pageContext.getAttribute("profileStartTime_" + id);
        profiler.child(id).record(endTime - startTime);
        return SKIP_BODY;
    }
}
