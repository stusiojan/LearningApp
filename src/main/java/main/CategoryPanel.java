package main;

import main.lib.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;

public class CategoryPanel extends JPanel implements ActionListener, TreeSelectionListener {

    private User user;
    private JTextField nodeNameField;
    private JButton addButton;
    private JButton editButton;
    private JButton refreshButton;
    private JCheckBox onlyForUserCheckBox;
    private JTree categoryTree;
    private DataNode selectedNode;
    private CategoryTreeRenderer categoryTreeRenderer;
    
    public CategoryPanel(User user) {
        this.user = user;
        nodeNameField = new JTextField(25);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        editButton = new JButton("Edit");
        editButton.addActionListener(this);
        refreshButton = new JButton("↻");
        refreshButton.addActionListener(this);
        onlyForUserCheckBox = new JCheckBox("Only show categories with tasks for this user.", false);
        onlyForUserCheckBox.addActionListener(this);
        categoryTreeRenderer = new CategoryTreeRenderer();
        rebuildUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final int MAX_NAME_LENGTH = 50;
        var treeModel = (DefaultTreeModel)categoryTree.getModel();

        if (e.getSource() == refreshButton) {
            rebuildUI();
        }
        else if (e.getSource() == addButton && selectedNode != null) {
            String name = nodeNameField.getText();
            if (name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
                JOptionPane.showMessageDialog(this, "The name must be 1 - " + MAX_NAME_LENGTH + " characters long.");
                return;
            }
            DataNode child = selectedNode.addChild(user, name);
            selectedNode.reload();
            treeModel.insertNodeInto(child, selectedNode, selectedNode.getChildCount());
            
            nodeNameField.setText("");
        }
        else if (e.getSource() == editButton && selectedNode != null) {
            //JPanel editPanel = selectedNode.getEditor();

            var window = (JFrame)SwingUtilities.windowForComponent(this);
            JDialog dialog = selectedNode.getEditor(window); //new JDialog(window, "Edit", true);
            //dialog.getContentPane().add(editPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            selectedNode.reload();

            if (selectedNode.getUserObject() == null) {
                // This node has been deleted
                treeModel.removeNodeFromParent(selectedNode);
                //treeModel.reload(selectedNode.getParent());
            } 
            else {
                treeModel.reload(selectedNode);
            }
        }
        else if (e.getSource() == onlyForUserCheckBox) {
            rebuildUI();
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath selPath = e.getPath();
        selectedNode = (DataNode)selPath.getLastPathComponent();

        if (selectedNode instanceof TaskNode) {
            addButton.setEnabled(false);
        }
        else {
            addButton.setEnabled(true);
        }
        editButton.setEnabled(true);
    }

    public void mouseClicked(MouseEvent e) {
        TreePath treePath = categoryTree.getClosestPathForLocation(e.getX(), e.getY());

        if (treePath != null) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                
            }
        }
    }

    public void rebuildUI() {
        removeAll();
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEADING));
        controls.add(nodeNameField);
        controls.add(addButton);
        controls.add(editButton);
        controls.add(refreshButton);
        controls.add(onlyForUserCheckBox);
        addButton.setEnabled(false);
        editButton.setEnabled(false);

        selectedNode = null;
        var root = new RootNode();

        Category[] categories = DatabaseManager.getCategories();
        for (var category : categories) {
            var categoryNode = new CategoryNode(category);

            Milestone[] milestones = DatabaseManager.getMilestones(category.getId(), user.getId());
            for (var milestone : milestones) {
                var milestoneNode = new MilestoneNode(milestone);
                
                Task[] tasks = DatabaseManager.getTasks(milestone.getId());
                for (var task : tasks) {
                    milestoneNode.add(new TaskNode(task));
                }
                categoryNode.add(milestoneNode);
            }
            if (!(onlyForUserCheckBox.isSelected() && milestones.length == 0))
                root.add(categoryNode);
        }

        categoryTree = new JTree(root);
        categoryTree.setRowHeight(categoryTree.getRowHeight() + 4);
        JScrollPane treePane = new JScrollPane(categoryTree);
        setLayout(new BorderLayout());
        add(controls, BorderLayout.PAGE_START);
        add(treePane, BorderLayout.CENTER);

        categoryTree.addTreeSelectionListener(this);
        categoryTree.setCellRenderer(categoryTreeRenderer);

        var thisPanel = this;
        categoryTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { thisPanel.mouseClicked(e); }
        });
    }

    private String colored(String text, String color) {
        final String SPAN_FORMAT = "<span style='color:%s;'>%s</span>";
        return String.format(SPAN_FORMAT, color, text);
    }

    /* 
     * Custom node types for JTree 
    */
    abstract class DataNode extends DefaultMutableTreeNode {
        public DataNode(Object name) {
            super(name);
        }
        public abstract void reload();
        public abstract DataNode addChild(User user, String name);
        public abstract JDialog getEditor(JFrame parent);
        public abstract String getText();
    }

    class RootNode extends DataNode {
        public RootNode() {
            super("*");
        }
        public void reload() {}
        public DataNode addChild(User user, String name) {
            int id = DatabaseManager.addCategory(name);
            return new CategoryNode(DatabaseManager.getCategory(id));
        }
        public JDialog getEditor(JFrame parent) { return null; }
        public String getText() { return "*"; }
    }

    class CategoryNode extends DataNode {
        private Category category;

        public CategoryNode(Category category) {
            super(category);
            this.category = category;
        }
        public void reload() {
            category = DatabaseManager.getCategory(category.getId());
            setUserObject(category);
            ((DataNode)getParent()).reload();
        }
        public DataNode addChild(User user, String name) {
            int id = DatabaseManager.addMilestone(name, user.getId(), category.getId());
            return new MilestoneNode(DatabaseManager.getMilestone(id));
        }
        public JDialog getEditor(JFrame parent) {
            return new EditCategoryDialog(parent, category);
        }
        public String getText() {
            String text = category.getName();
            text += colored(" [" + category.getTasksDone() + "/" + category.getTasksAll() + "]", "gray");
            return text;
        }
    };

    class MilestoneNode extends DataNode {
        private Milestone milestone;

        public MilestoneNode(Milestone milestone) {
            super(milestone);
            this.milestone = milestone;
        }
        public void reload() {
            milestone = DatabaseManager.getMilestone(milestone.getId());
            setUserObject(milestone);
            ((DataNode)getParent()).reload();
        }
        public DataNode addChild(User user, String name) {
            int id = DatabaseManager.addTask(name, milestone.getId());
            return new TaskNode(DatabaseManager.getTask(id));
        }
        public JDialog getEditor(JFrame parent) {
            return new EditMilestoneDialog(parent, milestone);
        }
        public String getText() {
            String text = milestone.getName();
            text += colored(" [" + milestone.getTasksDone() + "/" + milestone.getTasksAll() + "]", "gray");
            return text;
        }
    };

    class TaskNode extends DataNode {
        private Task task;

        public TaskNode(Task task) {
            super(task);
            this.task = task;
        }
        public void reload() {
            task = DatabaseManager.getTask(task.getId());
            setUserObject(task);
            ((DataNode)getParent()).reload();
        }
        public DataNode addChild(User user, String name) { return null; }

        public JDialog getEditor(JFrame parent) {
            return new EditTaskDialog(parent, task);
        }
        public String getText() {
            String color = (task.getDateCompleted() != null) ? "green" : "red";
            return colored(task.getName(), color);
        }
    };

    public class CategoryTreeRenderer extends DefaultTreeCellRenderer {
       
        public CategoryTreeRenderer() {
        }
    
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            var node = (DataNode)value;
            //this.setIcon();  TODO?: icon
            this.setText("<html>" + node.getText() + "</html>");
            return this;
        }
    }
}