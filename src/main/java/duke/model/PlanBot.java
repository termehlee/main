package duke.model;

import duke.commons.LogsCenter;
import duke.exception.DukeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;


public class PlanBot {

    private static final Logger logger = LogsCenter.getLogger(PlanQuestion.class);

    /**
     * Denotes if the dialog is from the user or from the Bot.
     */
    public enum Agent {
        USER,
        BOT
    }

    /**
     * Contains a <code>List</code> of <code>PlanDialog</code> which is the chatBot's history.
     */
    private List<PlanDialog> dialogList;

    /**
     * The a <code>ObservableList</code> of <code>PlanDialog</code> history
     * so that the GUI can automatically be updated without having to repopulate the entire list.
     */
    private ObservableList<PlanDialog> dialogObservableList;

    /**
     * Contains all the <code>PlanQuestion</code> that we are going to ask the user.
     */
    private PlanQuestionBank planQuestionBank;

    /**
     * A Map of the user's attributes that we have already found out.
     */
    private Map<String, String> planAttributes;

    /**
     * The buffer of questions we are asking the user.
     */
    private Queue<PlanQuestion> questionQueue;

    /**
     * The current question being asked, contains null when no more questions are being asked.
     */
    private PlanQuestion currentQuestion;

    private PlanQuestionBank.PlanRecommendation planBudgetRecommendation;

    /**
     * Constructor for PlanBot.
     *
     * @param planAttributes the loaded attributes from Storage
     * @throws DukeException when there is an error loading questions based on the loaded planAttributes
     */
    public PlanBot(Map<String, String> planAttributes) {
        this.dialogList = new ArrayList<>();
        dialogObservableList = FXCollections.observableList(dialogList);
        this.planAttributes = planAttributes;
        this.questionQueue = new LinkedList<>();
        if (!planAttributes.isEmpty()) {
            StringBuilder knownAttributes = new StringBuilder("Here's what I know about you: \n");
            for (String key : planAttributes.keySet()) {
                knownAttributes.append(key + " : " + planAttributes.get(key) + "\n");
            }
            PlanDialog knownDialog = new PlanDialog(knownAttributes.toString(), Agent.BOT);
            dialogObservableList.add(knownDialog);
        }else {
            dialogObservableList.add(new PlanDialog("Hi, seems like this is the first time using Duke++. "
                    + "Let me plan your budget for you!"
                    + " Alternatively, type \"goto expense\" to start using Duke++!"
                    ,Agent.BOT));
        }
        try {
            planQuestionBank = new PlanQuestionBank();
        } catch (DukeException e) {
            dialogObservableList.add(new PlanDialog(e.getMessage(), Agent.BOT));
        }
        try {
            questionQueue.addAll(planQuestionBank.getQuestions(planAttributes));
        } catch (DukeException e) {
            dialogObservableList.add(new PlanDialog(e.getMessage(), Agent.BOT));
        }
        if (questionQueue.isEmpty()) {
            currentQuestion = null;
            try {
                dialogList.add(new PlanDialog(planQuestionBank
                        .makeRecommendation(planAttributes)
                        .getRecommendation(), Agent.BOT));
                planBudgetRecommendation = planQuestionBank
                        .makeRecommendation(planAttributes);
                dialogList.add(new PlanDialog("Here's a recommended budget for you, "
                        + "type \"export\" to export the budget",
                        Agent.BOT));
            } catch (DukeException e) {
                dialogList.add(new PlanDialog(e.getMessage(), Agent.BOT));
            }
        } else {
            PlanQuestion firstQuestion = questionQueue.peek();
            currentQuestion = firstQuestion;
            questionQueue.remove();
            PlanDialog initial = new PlanDialog(firstQuestion.getQuestion(), Agent.BOT);
            dialogObservableList.add(initial);
        }
    }

    public ObservableList<PlanDialog> getDialogObservableList() {
        return dialogObservableList;
    }

    /**
     * Processes the input String of the user.
     *
     * @param input the input String of the user
     * @throws DukeException when there is invalid input
     */
    public void processInput(String input) throws DukeException {
        dialogObservableList.add(new PlanDialog(input, Agent.USER));
        if (currentQuestion == null) {
            PlanDialog emptyQueueDialog = new PlanDialog("Based on what you've told me, "
                    + "here's a recommended budget plan!", Agent.BOT);
            dialogObservableList.add(emptyQueueDialog);
            dialogObservableList.add(new PlanDialog(planQuestionBank.makeRecommendation(planAttributes)
                    .getRecommendation(),
                    Agent.BOT));
            planBudgetRecommendation = planQuestionBank.makeRecommendation(planAttributes);
            dialogList.add(new PlanDialog("Here's a recommended budget for you, "
                    + "type \"export\" to export the budget", Agent.BOT));
        } else {
            try {
                PlanQuestion.Reply reply = currentQuestion.getReply(input, planAttributes);
                questionQueue.clear();
                questionQueue.addAll(planQuestionBank.getQuestions(planAttributes));
                logger.info("\n\n\nQueue size: " + questionQueue.size());

                dialogObservableList.add(new PlanDialog(reply.getText(), Agent.BOT));
                planAttributes = reply.getAttributes();
                if (!questionQueue.isEmpty()) {
                    currentQuestion = questionQueue.peek();
                    dialogObservableList.add(new PlanDialog(currentQuestion.getQuestion(), Agent.BOT));
                    questionQueue.remove();
                } else {
                    dialogObservableList.add(new PlanDialog(planQuestionBank.makeRecommendation(planAttributes)
                            .getRecommendation(),
                            Agent.BOT));
                    planBudgetRecommendation = planQuestionBank.makeRecommendation(planAttributes);
                    dialogList.add(new PlanDialog("Here's a recommended budget for you, "
                            + "type \"export\" to export the budget", Agent.BOT));
                }
            } catch (DukeException e) {
                dialogObservableList.add(new PlanDialog(e.getMessage(), Agent.BOT));
            }
        }
    }

    public PlanQuestionBank.PlanRecommendation getPlanBudgetRecommendation() {
        return planBudgetRecommendation;
    }

    public Map<String, String> getPlanAttributes() {
        return planAttributes;
    }


    /**
     * A container for an individual chat history.
     */
    public class PlanDialog {
        public String text;
        public Agent agent;

        public PlanDialog(String text, Agent agent) {
            this.agent = agent;
            this.text = text;
        }
    }


}