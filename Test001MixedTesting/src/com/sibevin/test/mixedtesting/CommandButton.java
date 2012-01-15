package com.sibevin.test.mixedtesting;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

abstract class CommandButton extends ImageButton {
	
	public static enum CommandType {
		COPY,
		PASTE,
		DELETE,
		CUT,
		RENAME
	}
	
	public static CommandButton createButton(CommandType initType, Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr) {
		switch(initType) {
			case COPY:
				return new CopyCommandButton(context, commandBhr, hintBhr);
			case PASTE:
				return new PasteCommandButton(context, commandBhr, hintBhr);
			case DELETE:
				return new DeleteCommandButton(context, commandBhr, hintBhr);
			case CUT:
				return new CutCommandButton(context, commandBhr, hintBhr);
			case RENAME:
				return new RenameCommandButton(context, commandBhr, hintBhr);
			default:
				return new CopyCommandButton(context, commandBhr, hintBhr);
		}
	}

	public CommandButton(Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr, int imageResId, CommandType type) {
		super(context);
		commandBehavior = commandBhr;
		hintBehavior = hintBhr;
		this.setImageResource(imageResId);
		this.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				runCommand();
			}
		});
		this.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				if(hintBehavior == null) {
					return false;
				} else {
					runHint();
					return true;
				}
			}
		});
	}
	
	public CommandType getType() {
		return type;
	}

	protected abstract void runCommand();
	protected abstract void runHint();
	protected FileExploreBehavior commandBehavior;
	protected FileExploreBehavior hintBehavior;
	protected CommandType type;
}

class CopyCommandButton extends CommandButton {

	public CopyCommandButton(Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr) {
		super(context, commandBhr, hintBhr, R.drawable.s40_command_copy, CommandType.COPY);
	}

	@Override
	protected void runCommand() {
		commandBehavior.copy();
	}

	@Override
	protected void runHint() {
		hintBehavior.copy();
	}
}

class PasteCommandButton extends CommandButton {

	public PasteCommandButton(Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr) {
		super(context, commandBhr, hintBhr, R.drawable.s40_command_paste, CommandType.PASTE);
	}

	@Override
	protected void runCommand() {
		commandBehavior.paste();
	}

	@Override
	protected void runHint() {
		hintBehavior.paste();
	}
}

class DeleteCommandButton extends CommandButton {

	public DeleteCommandButton(Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr) {
		super(context, commandBhr, hintBhr, R.drawable.s40_command_delete, CommandType.DELETE);
	}

	@Override
	protected void runCommand() {
		commandBehavior.delete();
	}

	@Override
	protected void runHint() {
		hintBehavior.delete();
	}
}

class CutCommandButton extends CommandButton {

	public CutCommandButton(Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr) {
		super(context, commandBhr, hintBhr, R.drawable.s40_command_cut, CommandType.CUT);
	}

	@Override
	protected void runCommand() {
		commandBehavior.cut();
	}

	@Override
	protected void runHint() {
		hintBehavior.cut();
	}
}

class RenameCommandButton extends CommandButton {

	public RenameCommandButton(Context context, FileExploreBehavior commandBhr, FileExploreBehavior hintBhr) {
		super(context, commandBhr, hintBhr, R.drawable.s40_command_rename, CommandType.RENAME);
	}

	@Override
	protected void runCommand() {
		commandBehavior.rename();
	}

	@Override
	protected void runHint() {
		hintBehavior.rename();
	}
}
