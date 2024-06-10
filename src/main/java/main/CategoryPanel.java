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
        categoryTreeRenderer = new CategoryTreeRenderer();
        rebuildUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final int MAX_NAME_LENGTH = 50;

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
            var treeModel = (DefaultTreeModel)categoryTree.getModel();
            treeModel.insertNodeInto(child, selectedNode, selectedNode.getChildCount());
            
            nodeNameField.setText("");
        }
        else if (e.getSource() == editButton && selectedNode != null) {
            selectedNode.edit(this);
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
            DataNode node = (DataNode)treePath.getLastPathComponent();

            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                //node.edit(this);
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                
            }
        }
    }

    private void rebuildUI() {
        removeAll();
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEADING));
        controls.add(nodeNameField);
        controls.add(addButton);
        controls.add(editButton);
        controls.add(refreshButton);
        addButton.setEnabled(false);
        editButton.setEnabled(false);

        selectedNode = null;
        var root = new RootNode("*");

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
            root.add(categoryNode);
        }

        categoryTree = new JTree(root);
        JScrollPane treePane = new JScrollPane(categoryTree);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(controls);
        add(treePane);
        add(Box.createRigidArea(new Dimension(0,20)));

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
        public DataNode(String name) {
            super(name);
        }
        public abstract void reload();
        public abstract DataNode addChild(User user, String name);
        public abstract void edit(Component parent);
        public abstract String getText();
    }

    class RootNode extends DataNode {
        public RootNode(String name) {
            super(name);
        }
        public void reload() {}
        public DataNode addChild(User user, String name) {
            int id = DatabaseManager.addCategory(name);
            return new CategoryNode(DatabaseManager.getCategory(id));
        }
        public void edit(Component parent) {}
        public String getText() { return "*"; }
    }

    class CategoryNode extends DataNode {
        private Category category;

        public CategoryNode(Category category) {
            super(category.getName());
            this.category = category;
        }
        public void reload() {
            category = DatabaseManager.getCategory(category.getId());
        }
        public DataNode addChild(User user, String name) {
            int id = DatabaseManager.addMilestone(name, user.getId(), category.getId());
            return new MilestoneNode(DatabaseManager.getMilestone(id));
        }
        public void edit(Component parent) {
            // TODO: editor modal
            JOptionPane.showMessageDialog(parent, "Edit category: " + category.getName());
        }
        public String getText() {
            String text = category.getName();
            text += colored(" (" + category.getTasksAll() + ")", "gray");
            text += colored(" (" + category.getTasksDone() + ")", "green");
            return text;
        }
    };

    class MilestoneNode extends DataNode {
        private Milestone milestone;

        public MilestoneNode(Milestone milestone) {
            super(milestone.getName());
            this.milestone = milestone;
        }
        public void reload() {
            milestone = DatabaseManager.getMilestone(milestone.getId());
        }
        public DataNode addChild(User user, String name) {
            int id = DatabaseManager.addTask(name, milestone.getId());
            return new TaskNode(DatabaseManager.getTask(id));
        }
        public void edit(Component parent) {
            // TODO: editor modal
            JOptionPane.showMessageDialog(parent, "Edit milestone: " + milestone.getName());
        }
        public String getText() {
            String text = milestone.getName();
            text += colored(" (" + milestone.getTasksAll() + ")", "gray");
            text += colored(" (" + milestone.getTasksDone() + ")", "green");
            return text;
        }
    };

    class TaskNode extends DataNode {
        private Task task;

        public TaskNode(Task task) {
            super(task.getName());
            this.task = task;
        }
        public void reload() {
            task = DatabaseManager.getTask(task.getId());
        }
        public DataNode addChild(User user, String name) { return null; }

        public void edit(Component parent) {
            // TODO: editor modal
            JOptionPane.showMessageDialog(parent, "Edit task: " + task.getName());
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