package com.bnfd.overseer.utils;

import com.bnfd.overseer.model.constants.ActionType;
import com.bnfd.overseer.model.persistence.ActionEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ActionUtils {
    public static ActionType getActionType(String action) {
        boolean isBoolean = "true".equalsIgnoreCase(action) || "false".equalsIgnoreCase(action);
        if (isBoolean) {
            return ActionType.BOOLEAN;
        }

        boolean isCron = ValidationUtils.isValidCron(action);
        if (isCron) {
            return ActionType.CRON;
        }

        return ActionType.STRING;
    }

    public static Set<ActionEntity> syncActions(String referenceId, Set<ActionEntity> currentActions, Set<ActionEntity> requestedActions, boolean isNew) {
        Set<ActionEntity> actions = new HashSet<>();

        if (CollectionUtils.isEmpty(currentActions) && CollectionUtils.isEmpty(requestedActions)) {
            return actions;
        } else if (CollectionUtils.isEmpty(requestedActions)) {
            currentActions.forEach(action -> {
                action.setId(UUID.randomUUID().toString());
                action.setReferenceId(referenceId);
            });
            actions.addAll(currentActions);
        } else {
            for (ActionEntity request : requestedActions) {
                ActionEntity currentAction = currentActions.stream().filter(action -> action.getName().equalsIgnoreCase(request.getName())).findFirst().orElse(null);

                if (ObjectUtils.isEmpty(currentAction)) {
                    // new action
                    currentAction = new ActionEntity();
                    currentAction.setId(UUID.randomUUID().toString());
                    currentAction.setReferenceId(referenceId);
                    currentAction.setName(request.getName());
                    currentAction.setVal(request.getVal());
                    currentAction.setType(ActionUtils.getActionType(request.getVal()));
                } else {
                    if (isNew) {
                        // new uuid, and maintain reference id
                        currentAction.setId(UUID.randomUUID().toString());
                        currentAction.setReferenceId(referenceId);
                    } else {
                        currentAction.setVal(request.getVal());
                    }
                }

                actions.add(currentAction);
            }
        }

        return actions;
    }
}
