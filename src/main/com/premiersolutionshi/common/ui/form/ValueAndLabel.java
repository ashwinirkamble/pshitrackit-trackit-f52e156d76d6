package com.premiersolutionshi.common.ui.form;

/**
 * This is a generic object used to build select dropdowns, a list of radio buttons.
 */
public class ValueAndLabel {
    public ValueAndLabel(String value, String label) {
        super();
        this.value = value;
        this.label = label;
    }

    private String value;
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
