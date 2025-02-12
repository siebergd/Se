package net.hydrogen2oxygen.se.protocol;

import j2html.tags.DomContent;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.H1Tag;
import j2html.tags.specialized.SpanTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Every automation generates a protocol
 */
public class Protocol {

    private final List<ProtocolEntry> protocolEntryList = new ArrayList<>();

    private String title;
    private String screenshotPath;
    private String protocolsPath;

    public enum ProtocolType {
        H1,H2,H3,H4,PARAGRAPH,DEBUG,INFO,WARNING,ERROR,PRECONDITION_FAIL,SCREENSHOT,
        SCREENSHOT_WITH_DESCRIPTION,ASSERT_SUCCESS,ASSERT_FAIL,UNEXPECTED_TECHNICAL_ERROR,
        SKIP
    }

    public enum ProtocolResult {
        SUCCESS, FAIL, PRECONDITION_FAIL, TECHNICAL_ERROR, SKIPPED, UNKNOWN
    }

    public void h1(String title) {
        add(ProtocolType.H1, title);
    }

    public void h2(String title) {
        add(ProtocolType.H2, title);
    }

    public void h3(String title) {
        add(ProtocolType.H3, title);
    }

    public void h4(String title) {
        add(ProtocolType.H4, title);
    }

    public void paragraph(String text) {
        add(ProtocolType.PARAGRAPH, text);
    }

    /**
     * Use "debug" for every technical protocol. Debug level can be switched off globally.
     * @param message to protocol
     */
    public void debug(String message) {
        add(ProtocolType.DEBUG, message);
    }

    public void info(String message) {
        add(ProtocolType.INFO, message);
    }

    public void warn(String message) {
        add(ProtocolType.WARNING, message);
    }

    public void error(String message) {
        add(ProtocolType.ERROR, message);
    }

    public void skip(String message) {
        add(ProtocolType.SKIP, message);
    }

    public void preconditionFail(String message) {
        add(ProtocolType.PRECONDITION_FAIL, message);
    }

    public void screenshot(String imageId) {
        add(ProtocolType.SCREENSHOT, imageId);
    }

    public void screenshot(String imageId, String description) {
        add(ProtocolType.SCREENSHOT_WITH_DESCRIPTION, imageId + "|" + description);
    }

    public void assertSuccess(String message) {
        add(ProtocolType.ASSERT_SUCCESS, message);
    }

    public void assertFail(String message) {
        add(ProtocolType.ASSERT_FAIL, message);
    }

    public void unexpectedTechnicalError(String message) {
        add(ProtocolType.UNEXPECTED_TECHNICAL_ERROR, message);
    }

    private void add(ProtocolType protocolType, String data) {
        protocolEntryList.add(new ProtocolEntry(protocolType, data));
    }

    public static class ProtocolEntry {

        private final ProtocolType protocolType;
        private final String data;

        public ProtocolEntry(ProtocolType protocolType, String data) {
            this.protocolType = protocolType;
            this.data = data;
        }

        public ProtocolType getProtocolType() {
            return protocolType;
        }

        public String getData() {
            return data;
        }

        public DomContent getDomContent() {

            switch (this.protocolType) {
                case DEBUG:
                    return new SpanTag().withClass("debug").withText(data);
                case H1:
                    return new H1Tag().withText(data);
                default:
                    return new DivTag().withText(protocolType.name() + " - " + data);
            }
        }
    }

    public ProtocolResult getProtocolResult() {

        boolean assertSuccess = false;

        for (ProtocolEntry entry : protocolEntryList) {

            switch (entry.protocolType) {
                case UNEXPECTED_TECHNICAL_ERROR:
                    return ProtocolResult.TECHNICAL_ERROR;
                case ASSERT_FAIL:
                    return ProtocolResult.FAIL;
                case PRECONDITION_FAIL:
                    return ProtocolResult.PRECONDITION_FAIL;
                case SKIP:
                    return ProtocolResult.SKIPPED;
                case ASSERT_SUCCESS:
                    assertSuccess = true;
                    break;
                default:
                    // nothing (all other types are no "result types")
            }
        }

        if (assertSuccess) return ProtocolResult.SUCCESS;

        return ProtocolResult.UNKNOWN;
    }

    public List<ProtocolEntry> getProtocolEntryList() {
        return protocolEntryList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public String getProtocolsPath() {
        return protocolsPath;
    }

    public void setProtocolsPath(String protocolsPath) {
        this.protocolsPath = protocolsPath;
    }
}
