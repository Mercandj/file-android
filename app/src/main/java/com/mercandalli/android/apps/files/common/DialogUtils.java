package com.mercandalli.android.apps.files.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

@SuppressWarnings("unused")
public final class DialogUtils {

    private DialogUtils() {
        // Non-instantiable.
    }

    public static void seekBarDialog(
            @Nullable final Context context,
            @NonNull final String title,
            final int initValue,
            final int maxValue,
            @Nullable final String positive,
            @Nullable OnDialogUtilsSeekBarListener onDialogUtilsSeekBarListenerParam) {
        if (context == null) {
            return;
        }
        final WeakReference<OnDialogUtilsSeekBarListener> onDialogUtilsSeekBarListenerReference =
                new WeakReference<>(onDialogUtilsSeekBarListenerParam);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);

        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);

        final TextView valueText = new TextView(context);
        valueText.setGravity(Gravity.CENTER_HORIZONTAL);
        valueText.setTextSize(32);
        layout.addView(valueText, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        final SeekBar seekBar = new SeekBar(context);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                final OnDialogUtilsSeekBarListener onDialogUtilsSeekBarListener =
                        onDialogUtilsSeekBarListenerReference.get();
                if (onDialogUtilsSeekBarListener == null) {
                    valueText.setText(progress);
                } else {
                    valueText.setText(onDialogUtilsSeekBarListener.onDialogUtilsSeekBarChanged(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        });
        seekBar.setMax(maxValue);
        seekBar.setProgress(initValue);
        layout.addView(seekBar, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        if (onDialogUtilsSeekBarListenerParam == null) {
            valueText.setText(initValue);
        } else {
            valueText.setText(onDialogUtilsSeekBarListenerParam.onDialogUtilsSeekBarChanged(initValue));
        }
        builder.setView(layout);
        builder.setPositiveButton(positive, (dialog, which) -> {
            final OnDialogUtilsSeekBarListener onDialogUtilsSeekBarListener =
                    onDialogUtilsSeekBarListenerReference.get();
            if (onDialogUtilsSeekBarListener != null) {
                onDialogUtilsSeekBarListener.onDialogUtilsSeekBarCalledBack(
                        seekBar.getProgress());
            }
            dialog.dismiss();
        });
        builder.create().show();
    }

    public static void listDialog(
            @Nullable final Context context,
            @NonNull final String title,
            @NonNull final List<String> items,
            @Nullable DialogInterface.OnClickListener listener) {
        if (context == null) {
            return;
        }
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (listener == null) {
            builder.setItems(items.toArray(new String[items.size()]),
                    (dialog, which) -> {

                    });
        } else {
            builder.setItems(items.toArray(new String[items.size()]), listener);
        }
        builder.create().show();
    }

    public static void alert(
            @Nullable final Context context,
            final String title,
            final String message,
            @Nullable final String positive,
            @Nullable final OnDialogUtilsListener positiveListenerParam,
            @Nullable final String negative,
            @Nullable final OnDialogUtilsListener negativeListenerParam) {
        if (context == null) {
            return;
        }
        final WeakReference<OnDialogUtilsListener> positiveListenerReference =
                new WeakReference<>(positiveListenerParam);
        final WeakReference<OnDialogUtilsListener> negativeListenerReference =
                new WeakReference<>(negativeListenerParam);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if (positive != null) {
            builder.setPositiveButton(positive, (dialog, which) -> {
                final OnDialogUtilsListener positiveListener = positiveListenerReference.get();
                if (positiveListener != null) {
                    positiveListener.onDialogUtilsCalledBack();
                }
                dialog.dismiss();
            });
        }
        if (negative != null) {
            builder.setNegativeButton(negative, (dialog, which) -> {
                final OnDialogUtilsListener negativeReference = negativeListenerReference.get();
                if (negativeReference != null) {
                    negativeReference.onDialogUtilsCalledBack();
                }
                dialog.dismiss();
            });
        }
        builder.create().show();
    }

    public static void alert(
            final Context context,
            final String title,
            final Spanned message,
            final String positive,
            final OnDialogUtilsListener positiveListenerParam,
            final String negative,
            final OnDialogUtilsListener negativeListenerParam) {

        final WeakReference<OnDialogUtilsListener> positiveListenerReference =
                new WeakReference<>(positiveListenerParam);
        final WeakReference<OnDialogUtilsListener> negativeListenerReference =
                new WeakReference<>(negativeListenerParam);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if (positive != null) {
            builder.setPositiveButton(positive, (dialog, which) -> {
                final OnDialogUtilsListener positiveListener = positiveListenerReference.get();
                if (positiveListener != null) {
                    positiveListener.onDialogUtilsCalledBack();
                }
                dialog.dismiss();
            });
        }
        if (negative != null) {
            builder.setNegativeButton(negative, (dialog, which) -> {
                final OnDialogUtilsListener negativeListener = negativeListenerReference.get();
                if (negativeListener != null) {
                    negativeListener.onDialogUtilsCalledBack();
                }
                dialog.dismiss();
            });
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void prompt(
            final Context context,
            final String title,
            final String message,
            final String positive,
            final OnDialogUtilsStringListener positiveListener,
            final String negative,
            final OnDialogUtilsListener negativeListener) {
        prompt(
                context,
                title,
                message,
                positive,
                positiveListener,
                negative,
                negativeListener,
                null,
                null);
    }

    public static void prompt(
            final Context context,
            final String title,
            final String message,
            final String positive,
            final OnDialogUtilsStringListener positiveListener,
            final String negative,
            final OnDialogUtilsListener negativeListener,
            @Nullable final OnDialogUtilsListener dismissListener) {
        prompt(
                context,
                title,
                message,
                positive,
                positiveListener,
                negative,
                negativeListener,
                null,
                dismissListener);
    }

    public static void prompt(
            final Context context,
            final String title,
            final String message,
            final String positive,
            final OnDialogUtilsStringListener positiveListener,
            final String negative,
            final OnDialogUtilsListener negativeListener,
            @Nullable final String preTex,
            @Nullable final OnDialogUtilsListener dismissListener) {
        prompt(
                context,
                title,
                message,
                positive,
                positiveListener,
                negative,
                negativeListener,
                preTex,
                null,
                dismissListener);
    }

    public static void prompt(
            final Context context,
            final String title,
            final String message,
            final String positive,
            @Nullable final OnDialogUtilsStringListener positiveListener,
            final String negative,
            @Nullable final OnDialogUtilsListener negativeListener,
            @Nullable final String preText,
            @Nullable final String hint,
            @Nullable final OnDialogUtilsListener dismissListener) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        if (message != null) {
            alert.setMessage(message);
        }

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        if (preText != null) {
            input.setText(preText);
        }
        if (hint != null) {
            input.setHint(hint);
        }

        alert.setPositiveButton(positive, (dialog, whichButton) -> {
                    if (positiveListener != null) {
                        positiveListener.onDialogUtilsStringCalledBack(input.getText().toString());
                    }
                }
        );
        alert.setNegativeButton(negative, (dialog, whichButton) -> {
                    if (negativeListener != null) {
                        negativeListener.onDialogUtilsCalledBack();
                    }
                }
        );
        alert.setOnDismissListener(dialog -> {
            if (dismissListener != null) {
                dismissListener.onDialogUtilsCalledBack();
            }
        });

        //alert.show();
        final AlertDialog alertDialog = alert.create();
        alertDialog.setView(input, 38, 20, 38, 0);
        alertDialog.show();
    }

    public interface OnDialogUtilsListener {
        void onDialogUtilsCalledBack();
    }

    public interface OnDialogUtilsStringListener {
        void onDialogUtilsStringCalledBack(String text);
    }

    public interface OnDialogUtilsSeekBarListener {
        void onDialogUtilsSeekBarCalledBack(int value);

        @NonNull
        String onDialogUtilsSeekBarChanged(int value);
    }
}
