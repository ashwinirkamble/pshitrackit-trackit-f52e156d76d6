package com.premiersolutionshi.common.constant;

/**
 * This will store common messages based on the "msg" paramenter.
 */
public enum CommonMessage {
    SAVE_SUCCESS("saveSuccess", "Successfully saved<additional>.", MessageType.INFO)
    , SAVE_FAILED("saveFail", "Failed to save<additional>.", MessageType.ERROR)
    , DELETE_SUCCESS("deleteSuccess", "Successfully deleted #<id> <additional>.", MessageType.INFO)
    , DELETE_FAILED("deleteFail", "Failed to delete #<id> <additional>.", MessageType.ERROR)
    , NONE_SELECTED("noneSelected", "Nothing was selected to perform the operation.", MessageType.WARNING)
    , INFO("info", "<additional>", MessageType.INFO)
    , WARNING("warning", "<additional>", MessageType.WARNING)
    , ERROR("error", "<additional>", MessageType.ERROR)
    , NOT_FOUND("notFound", "ID #<id> not found.", MessageType.ERROR)
    ;

    private String key;
    private String message;
    private MessageType type;

    private CommonMessage(String key, String message, MessageType type) {
        this.key = key;
        this.message = message;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return getMessage(null, null);
    }

    public String getMessage(String additional, Integer id) {
        String formattedMessage = message
                .replaceAll("<id>", id == null ? "" : "" + id)
                .replaceAll("<additional>", additional == null ? "" : " " + additional);
        return formattedMessage;
    }

    public MessageType getType() {
        return type;
    }

    public static CommonMessage getByKey(String key) {
        CommonMessage[] values = values();
        for (CommonMessage commonMessage : values) {
            if (commonMessage.getKey().equals(key)) {
                return commonMessage;
            }
        }
        return INFO;
    }

    public static String getMessage(String key, String additional, Integer id) {
        return getByKey(key).getMessage(additional, id);
    }
}
