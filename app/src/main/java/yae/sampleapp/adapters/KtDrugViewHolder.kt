package yae.sampleapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import yae.sampleapp.R
import yae.sampleapp.models.Drug
import yae.sampleapp.util.DateHelper

open class KtDrugViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    private val textDrugName: TextView = itemView.findViewById(R.id.textDrugName)
    private val textLastTaking: TextView = itemView.findViewById(R.id.textLastTaking)
    private val btnTakeDrug: Button = itemView.findViewById(R.id.btnTakeDrug)
    private val btnDrugItemMenu: Button = itemView.findViewById(R.id.btnDrugItemMenu)

    private var takingDateIsPretty = true
    private var drug:Drug? =null

    interface OnClickListener {
        fun click(view:View, drug:Drug)
    }

    var onTakeDrugClickListener: OnClickListener? = null

    var onItemMenuClickListener: OnClickListener? = null

    protected fun handleOnTakeDrugClick() {
        onTakeDrugClickListener?.click(btnTakeDrug, drug!!)
    }

    protected fun handleOnItemMenuClick() {
        onItemMenuClickListener?.click(btnDrugItemMenu, drug!!)
    }

    private fun toggleTakingDateFormat() {
        drug!!.lastTakingDate ?: return
        val lastTakingText =
            if (takingDateIsPretty) DateHelper.formatDate(drug!!.lastTakingDate) else DateHelper.prettyDate(
                drug!!.lastTakingDate
            )

        textLastTaking.text = lastTakingText
        takingDateIsPretty = !takingDateIsPretty

    }
    fun bind(drug:Drug) {
        this.drug = drug
        textDrugName.text = drug.name
        textLastTaking.text = DateHelper.prettyDate(drug.lastTakingDate)
    }
    companion object {
        fun create(parent: ViewGroup):KtDrugViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.drug_item, parent, false)
            val vh = KtDrugViewHolder(view)
            vh.btnTakeDrug.setOnClickListener {
                vh.handleOnTakeDrugClick()
            }
            vh.btnDrugItemMenu.setOnClickListener {
                vh.handleOnItemMenuClick()
            }
            vh.textLastTaking.setOnClickListener {
                vh.toggleTakingDateFormat()
            }
            return vh
        }
    }
}