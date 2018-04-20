package com.hhdb.csadmin.common.ui.textEdit;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.hhdb.csadmin.common.base.DefaultSet;
import com.hhdb.csadmin.common.util.DefaultSetting;

public class QueryEditorTextArea extends RSyntaxTextArea implements
		DocumentListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private ListPopup popup;
	private String prefix = "";
	private List<String> keylist;
	private List<String> tablelist = new ArrayList<String>();
	private List<String> viewlist = new ArrayList<String>();
	private int pos = 0;
	private static final String COMMIT_ACTION = "commit";
	private String keyStr = "k";
	//===============================================
	public DefaultSet textpaneSet;
	//===============================================
	public void setTablelist(List<String> tablelist) {
		this.tablelist.clear();
		this.tablelist.addAll(tablelist);
	}

	public void setViewlist(List<String> viewlist) {
		this.viewlist.clear();
		this.viewlist.addAll(viewlist);
	}

	public QueryEditorTextArea(int arg0,int arg1){
		super(arg0, arg1);
		initTextArea();
	}
	
	public QueryEditorTextArea() {
		initTextArea();
	}
	
	private void initTextArea(){
		textpaneSet = DefaultSetting.loadFontSettings();
		keylist = textpaneSet.getKeylist();
		Font font = new Font("宋体", Font.PLAIN, Integer.parseInt(textpaneSet
				.getFontSize()));
		setFont(font);
		setBackground(DefaultSetting.strToColor(textpaneSet.getBackground()));
		
		addKeyListener(this);
		popup = new ListPopup();
		getDocument().addDocumentListener(this);

		// 创建快捷键(回车选择提示词)
		InputMap im = this.getInputMap();
		ActionMap am = this.getActionMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION); // 绑定回车键
		am.put(COMMIT_ACTION, new CommitAction());
		popup.list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && popup.isSelected()) {
					selectedValue();
				}
			}
		});
	}

	/**
	 * 智能提示功能
	 * 
	 * @param array
	 * @return
	 */
	private boolean isListChange(List<String> array) {
		if (array.size() != popup.getItemCount()) {
			return true;
		}
		for (int i = 0; i < array.size(); i++) {
			if (!((String) array.get(i)).equals(popup.getItem(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 搜索内容改变
	 * 
	 * @param text
	 */
	private void textChanged(String text) {
//		if ("".equals(text)) {
//			popup.setVisible(false);
//		} else {
			if (!popup.isVisible()) {
				showPopup();
				requestFocus();
			}
			changeList(getKeyWord(text));
//		}
	}

	/**
	 * 获得与输入内容匹配的提示内容集合
	 * 
	 * @param text
	 * @return
	 */
	private List<String> getKeyWord(String text) {
		List<String> strlist = keylist;
		if(keyStr.equals("t")){
			strlist = tablelist;
		}else if(keyStr.equals("w")){
			strlist = viewlist;
		}
		if(text.isEmpty()){
			return strlist;
		}
		List<String> list = new ArrayList<String>();
		for (String sqlkey : strlist) {
			if (sqlkey.toLowerCase().startsWith(text.toLowerCase()))
				list.add(sqlkey);
		}
		return list;
	}

	/**
	 * 显示提示内容框
	 */
	private void showPopup() {
		popup.setPopupSize(300, 180);
		try {
			Rectangle r = modelToView(getCaretPosition() - prefix.length());
			popup.show(this, r.x, r.y + 20);
		} catch (Exception e) {
			try {
				Rectangle r = modelToView(getCaretPosition() - prefix.length()
						- 1);
				popup.show(this, r.x, r.y + 20);
			} catch (Exception ee) {
			}
		}
	}

	/**
	 * 改变提示内容集合后的操作
	 * 
	 * @param array
	 */
	private void changeList(List<String> array) {
		if (array.size() == 0) {
			if (popup.isVisible()) {
				popup.setVisible(false);
			}
		} else if (!popup.isVisible()) {
			showPopup();
		}
		if ((isListChange(array)) && (array.size() != 0)) {
			popup.setList(array);
			popup.setSelectedIndex(0);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (popup.isVisible()) {
			if (e.getLength() != 1) {
				return;
			}
			int posinsert = e.getOffset();
			String content = null;
			try {
				if(posinsert>101){
					content = getText(posinsert-100, 101);
				}else{
					content = getText(0, posinsert + 1);
				}
			} catch (BadLocationException be) {
				be.printStackTrace();
			}
			int w;

			for (w = content.length()-1; (w >= 0) && (!isCharacter(content.charAt(w))); w--)
				;
			pos = posinsert;
			prefix = content.substring(w + 1);
			textChanged(prefix);
		}
	}

	/**
	 * 判断是否是特殊字符
	 * 
	 * @param ca
	 * @return
	 */
	private boolean isCharacter(char ca) {
		String flag = "\n\t\r ";
		// System.out.println(flag.length());
		for (int i = 0; i < flag.length(); i++) {
			// System.out.println("ca:"+ca+"-------- flag"+flag.charAt(i));
			if (flag.charAt(i) == ca) {

				return true;
			}
		}
		return false;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (popup.isVisible()) {
			if (e.getLength() != 1) {
				return;
			}
			int posremove = e.getOffset();
			String content = null;
			try {
				if(posremove>101){
					content = getText(posremove-100, 100);
				}else{
					content = getText(0, posremove);
				}
			} catch (BadLocationException be) {
				be.printStackTrace();
			}
			int w;

			for (w = content.length() - 1; (w >= 0)
					&& (!isCharacter(content.charAt(w))); w--)
				;
			pos = posremove - 1;
			prefix = content.substring(w + 1);
			textChanged(prefix);
		}
	}
	/**
	 * 按快捷键之后显示提示框之前的逻辑
	 */
	private void showReminder(){
		int posinsert = getCaretPosition();
		String content = null;
		try {
			if(posinsert>101){
				content = getText(posinsert-100, 100);
			}else{
				content = getText(0, posinsert);
			}
			
		} catch (BadLocationException be) {
			be.printStackTrace();
		}
		int w;

		for (w = content.length()-1; (w >= 0) && (!isCharacter(content.charAt(w))); w--)
			;		
		prefix = content.substring(w + 1);
		pos = posinsert-1;
		textChanged(prefix);
	}

	/**
	 * 选取提示词之后的逻辑
	 */
	private void selectedValue() {
		String str = popup.getSelectedValue().toString();
		//String leftString = str.substring(prefix.length());

		SwingUtilities.invokeLater(new CompletionTask(str, pos));
		popup.setVisible(false);
	}

	/**
	 * 按回车选取提示词
	 * 
	 * @author hhxd
	 */
	private class CommitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ev) {
			if (popup.isVisible()) {
				if (popup.isSelected()) {
					selectedValue();
				}
			} else {
				replaceSelection("\n");
			}
		}
	}

	private class CompletionTask implements Runnable {
		String completion;
		int position;

		CompletionTask(String completion, int position) {
			this.completion = completion + " ";
			this.position = position;
		}

		public void run() {
			try {
				getDocument().remove(position-prefix.length()+1, prefix.length());
				getDocument().insertString(position-prefix.length()+1, completion, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			setCaretPosition(position-prefix.length()+1 + completion.length());
		}
	}

	/**
	 * 键盘按键事件
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) { // 下
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setSelectedIndex(0);
				else
					popup.setSelectedIndex(popup.getSelectedIndex() + 1);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP) { // 上
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setLastOneSelected();
				else
					popup.setSelectedIndex(popup.getSelectedIndex() - 1);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) { // 下一页
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setSelectedIndex(0);
				else
					popup.setSelectedIndex(popup.getSelectedIndex() + 5);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) { // 上一页
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setLastOneSelected();
				else
					popup.setSelectedIndex(popup.getSelectedIndex() - 5);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) { // 空格
			if (popup.isVisible()) {
				popup.setVisible(false);
			}
		} 

		if(!textpaneSet.getQkeyguanjian().isEmpty()&&!textpaneSet.getQkeyguanjian().equals("无效")){
			String upkey = textpaneSet.getQkeyguanjian().substring(0,textpaneSet.getQkeyguanjian().indexOf("+"));
			char keychar = textpaneSet.getQkeyguanjian().charAt(textpaneSet.getQkeyguanjian().length()-1);
			if(upkey.equals("Ctrl")){
				if(e.getModifiers()==2 && e.getKeyCode()==KeyEvent.getExtendedKeyCodeForChar(keychar)){
					keyStr = "k";
					showReminder();
				}
			}else if(upkey.equals("Alt")){
				if(e.isAltDown() && e.getKeyChar()==keychar){
					keyStr = "k";
					showReminder();
				}
			}else if(upkey.equals("Shift")){
				if(e.getModifiers()==1 && e.getKeyCode()==KeyEvent.getExtendedKeyCodeForChar((keychar+"").toUpperCase().charAt(0))){
					keyStr = "k";
					showReminder();
				}
			}
		}
		//=========================
		if(!textpaneSet.getQkeytablename().isEmpty()&&!textpaneSet.getQkeytablename().equals("无效")){
			String upkey = textpaneSet.getQkeytablename().substring(0,textpaneSet.getQkeytablename().indexOf("+"));
			char keychar = textpaneSet.getQkeytablename().charAt(textpaneSet.getQkeytablename().length()-1);
			if(upkey.equals("Ctrl")){
				if(e.getModifiers()==2 && e.getKeyCode()==KeyEvent.getExtendedKeyCodeForChar(keychar)){
					keyStr = "t";
					showReminder();
				}
			}else if(upkey.equals("Alt")){
				if(e.isAltDown() && e.getKeyChar()==keychar){
					keyStr = "t";
					showReminder();
				}
			}else if(upkey.equals("Shift")){
				if(e.getModifiers()==1 && e.getKeyCode()==KeyEvent.getExtendedKeyCodeForChar((keychar+"").toUpperCase().charAt(0))){
					keyStr = "t";
					showReminder();
				}
			}
		}
		//=========================
		if(!textpaneSet.getQkeyviewname().isEmpty()&&!textpaneSet.getQkeyviewname().equals("无效")){
			String upkey = textpaneSet.getQkeyviewname().substring(0,textpaneSet.getQkeyviewname().indexOf("+"));
			char keychar = textpaneSet.getQkeyviewname().charAt(textpaneSet.getQkeyviewname().length()-1);
			if(upkey.equals("Ctrl")){
				if(e.getModifiers()==2 && e.getKeyCode()==KeyEvent.getExtendedKeyCodeForChar(keychar)){
					keyStr = "w";
					showReminder();
				}
			}else if(upkey.equals("Alt")){
				if(e.isAltDown() && e.getKeyChar()==keychar){
					keyStr = "w";
					showReminder();
				}
			}else if(upkey.equals("Shift")){
				if(e.getModifiers()==1 && e.getKeyCode()==KeyEvent.getExtendedKeyCodeForChar((keychar+"").toUpperCase().charAt(0))){
					keyStr = "w";
					showReminder();
				}
			}
		}
		//===========
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	// 根据位置获取行号
	public int getLineByPosition(int position) {
		Element root = getDocument().getDefaultRootElement();
		int line = root.getElementIndex(position) + 1;
		return line;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}