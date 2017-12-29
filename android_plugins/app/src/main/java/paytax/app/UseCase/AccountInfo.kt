package paytax.app.UseCase

import android.content.Context

class AccountInfo(val context: Context) {
    fun fetch(accountNumber: Number,
              voucherNumber: Number,
              callback: (code: Int, content: String) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}