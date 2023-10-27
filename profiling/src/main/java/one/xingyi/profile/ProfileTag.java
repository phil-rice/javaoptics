package one.xingyi.profile;


import lombok.Getter;
import lombok.Setter;
import one.xingyi.interfaces.INanoTime;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

@Setter
@Getter
public class ProfileTag extends BodyTagSupport {

    private String id;
    private IProfile profiler;

    private long startTime;

    @Override
    public int doStartTag() throws JspException {
        INanoTime nanoTime = profiler.timer();
        startTime = nanoTime.nanoTime();
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            getBodyContent().writeOut(getPreviousOut());
        } catch (IOException e) {
            throw new JspException("Error writing body content", e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        INanoTime nanoTime = profiler.timer();
        IProfile child = profiler.child(id);
        child.record(nanoTime.nanoTime() - startTime);
        return EVAL_PAGE;
    }
}