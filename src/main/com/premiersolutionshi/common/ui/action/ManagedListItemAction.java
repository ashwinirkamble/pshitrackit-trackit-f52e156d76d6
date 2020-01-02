package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.constant.MessageType;
import com.premiersolutionshi.common.domain.ManagedListItem;
import com.premiersolutionshi.common.service.ManagedListItemService;
import com.premiersolutionshi.common.util.StringUtils;

public class ManagedListItemAction extends BaseAction {

    public ManagedListItemAction() {
        super(ManagedListItemAction.class);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = getAction();
        ManagedListItemService managedListItemService = getManagedListItemService();
        if (StringUtils.isEmpty(action)) {
            ManagedListItem formInput = (ManagedListItem) form;
            action = formInput.getAction();
            if (StringUtils.isEmpty(action)) {
                action = FORWARD_INDEX;
            }
        }
        if (action.equals(FORWARD_SAVE)) {
            handleSave(response, form);
        }
        else if (action.equals(FORWARD_DELETE)) {
            Integer id = StringUtils.parseInt(request.getParameter("id"));
            handleDelete(response, id);
        }
        else if (action.equals("currentDefault")) {
            handleCurrentDefault(request);
        }
        else if (action.equals("sortOrder")) {
            String pkListStr = request.getParameter("pkList");
            String[] pkArr = pkListStr.split(",");
            response.getWriter().write("{\"result\": " + managedListItemService.handeSorting(pkArr) + "}");
            return null;
        }
        ArrayList<ManagedList> managedLists;
        if (getProjectPk() == null) {
            managedLists = ManagedList.getGlobalOnly();
        }
        else {
            managedLists = ManagedList.getNotHidden();
        }
        Integer managedListCode = StringUtils.parseInt(request.getParameter("managedListCode"));
        ManagedList selectedManagedList;
        if (managedListCode != null) {
            selectedManagedList = ManagedList.getByCode(managedListCode);
        }
        else {
            selectedManagedList = managedLists.get(0);
        }
        Integer projectPk = getProjectPk();
        ArrayList<ManagedListItem> itemList = managedListItemService.getByListAndProjectFk(selectedManagedList, projectPk);
        request.setAttribute("projectPk", projectPk + "");
        request.setAttribute("selectedManagedList", selectedManagedList);
        request.setAttribute("itemList", itemList);
        request.setAttribute("managedLists", managedLists);
        return "index";
    }

    private void handleDelete(HttpServletResponse response, Integer id) throws IOException {
        ManagedListItemService managedListItemService = getManagedListItemService();
        ManagedListItem item = managedListItemService.getById(id);
        if (item != null) {
            String itemLabel = item.getManagedList().getItemLabel();
            String additionalParams = "managedListCode=" + item.getManagedListCode();
            if (managedListItemService.deleteById(id)) {
                redirectWithMessage(response, CommonMessage.DELETE_SUCCESS, itemLabel, additionalParams);
                return;
            }
        }
        redirectWithMessage(response, CommonMessage.DELETE_FAILED);
    }

    private void handleCurrentDefault(HttpServletRequest request) {
        ManagedListItemService managedListItemService = getManagedListItemService();
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (id == null) {
            setMessage("ID not found in the parameter.");
            return;
        }
        else {
            ManagedListItem domain = managedListItemService.getById(id);
            if (domain != null) {
                boolean success = true;
                ArrayList<ManagedListItem> items = managedListItemService.getByListAndProjectFk(domain.getManagedList(), getProjectPk());
                for (ManagedListItem managedListItem : items) {
                    if (managedListItem.isCurrentDefault()) {
                        managedListItem.setCurrentDefault(false);
                        if (!managedListItemService.save(managedListItem)) {
                            success = false;
                            break;
                        }
                    }
                }
                if (success) {
                    domain.setCurrentDefault(true);
                    if (!managedListItemService.save(domain)) {
                        success = false;
                    }
                }
                if (success) {
                    setMessage("Successfully set '" + domain.getItemValue() + "' to current/default.");
                }
                else {
                    setMessage("Failed to set '" + domain.getItemValue() + "' to current/default.", MessageType.ERROR);
                }
            }
            else {
                setMessage("Could not retreive domain by ID.", MessageType.ERROR);
            }
        }
    }

    private void handleSave(HttpServletResponse response, ActionForm form) throws IOException {
        ManagedListItemService managedListItemService = getManagedListItemService();
        ManagedListItem item = (ManagedListItem) form;
        String itemLabel = item.getManagedList().getItemLabel();
        String additionalParams = "managedListCode=" + item.getManagedListCode();
        if (managedListItemService.save(item)) {
            redirectWithMessage(response, CommonMessage.SAVE_SUCCESS, " " + itemLabel, additionalParams);
        }
        else {
            redirectWithMessage(response, CommonMessage.SAVE_FAILED, " " + itemLabel, additionalParams);
        }
    }

    @Override
    protected String path() {
        return "managedList.do";
    }
}
