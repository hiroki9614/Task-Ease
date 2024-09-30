package com.purpleye.taskease.ui.components

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.Locale

@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    selectionDate: LocalDate,
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current

    // 日付が選択されたときのコールバック
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val monthStr = String.format(Locale.JAPAN, "%02d", month + 1)
            val dayStr = String.format(Locale.JAPAN, "%02d", dayOfMonth)

            val selectedDateString = "$year/${monthStr}/$dayStr"
            Log.d("DatePickerDialog", "select date:  $selectedDateString")
            onDateSelected(selectedDateString)
        },
        selectionDate.year,
        selectionDate.monthValue - 1,
        selectionDate.dayOfMonth
    )

    // ダイアログ表示制御
    if (showDialog) {
        datePickerDialog.show()
        onDismissRequest() // ダイアログを表示したらフラグをリセット
    }
}