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
    private JButton refreshButton;
    private JTree categoryTree;
    private DataNode selectedNode;
    
    public CategoryPanel(User user) {
        this.user = user;
        nodeNameField = new JTextField(25);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        refreshButton = new JButton("↻");
        refreshButton.addActionListener(this);
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
            selectedNode.addChild(user, name);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath selPath = e.getPath();
        selectedNode = (DataNode)selPath.getLastPathComponent();
        addButton.setEnabled(true);
    }

    public void mouseClicked(MouseEvent e) {
        TreePath treePath = categoryTree.getClosestPathForLocation(e.getX(), e.getY());

        if (treePath != null) {
            DataNode node = (DataNode)treePath.getLastPathComponent();

            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                node.edit(this);
            }
            else if (e.getButton() == MouseEvent.BUTTON2) {
                System.out.println("Right click");
            }
        }
    }

    private void rebuildUI() {
        removeAll();
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEADING));
        controls.add(nodeNameField);
        controls.add(addButton);
        controls.add(refreshButton);
        addButton.setEnabled(false);

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

        var thisPanel = this;
        categoryTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { thisPanel.mouseClicked(e); }
        });
    }

    /* 
     * Custom node types for JTree 
    */
    abstract class DataNode extends DefaultMutableTreeNode {
        public DataNode(String name) {
            super(name);
        }
        public abstract void addChild(User user, String name);
        public abstract void edit(Component parent);
    }

    class RootNode extends DataNode {
        public RootNode(String name) {
            super(name);
        }
        public void addChild(User user, String name) {
            // TODO: add a new category
            System.out.println("Add category: " + name);
        }
        public void edit(Component parent) {}
    }

    class CategoryNode extends DataNode {
        private final Category category;

        public CategoryNode(Category category) {
            super(category.getName());
            this.category = category;
        }
        public void addChild(User user, String name) {
            // TODO: add a new milestone
            System.out.println("Add milestone: " + name);
        }
        public void edit(Component parent) {
            // TODO: editor modal
            JOptionPane.showMessageDialog(parent, "Edit category: " + category.getName());
        }
    };

    class MilestoneNode extends DataNode {
        private final Milestone milestone;

        public MilestoneNode(Milestone milestone) {
            super(milestone.getName());
            this.milestone = milestone;
        }
        public void addChild(User user, String name) {
            // TODO: add a new task
            System.out.println("Add task: " + name);
        }
        public void edit(Component parent) {
            // TODO: editor modal
            JOptionPane.showMessageDialog(parent, "Edit milestone: " + milestone.getName());
        }
    };

    class TaskNode extends DataNode {
        private final Task task;

        public TaskNode(Task task) {
            super(task.getName());
            this.task = task;
        }
        public void addChild(User user, String name) {}

        public void edit(Component parent) {
            // TODO: editor modal
            JOptionPane.showMessageDialog(parent, "Edit task: " + task.getName());
        }
    };
}