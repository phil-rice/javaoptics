package one.xingyi.profile;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DelayTag extends TagSupport {
    private int delay = 1000; // Default to 1000ms

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
