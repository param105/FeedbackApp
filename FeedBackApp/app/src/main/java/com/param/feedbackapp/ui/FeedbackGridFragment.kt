package com.param.feedbackapp.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.param.feedbackapp.R
import com.param.feedbackapp.draw.RenderActivity
import kotlinx.android.synthetic.main.fragment_feedback_grid.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

private const val REQUEST_CODE_DRAW = 101
private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102
class FirstFragment : Fragment() {

    lateinit var drawAdapter: DrawAdapter
    lateinit var rootView :View
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_feedback_grid, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        setFabClickEvent()
    }

    private fun setFabClickEvent() {
        fab_add.setOnClickListener {
            val intent = Intent(activity, RenderActivity::class.java)
            startActivityForResult(intent,
                REQUEST_CODE_DRAW
            )
        }
    }

    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }else{
            drawAdapter = DrawAdapter(
                this.requireContext(),
                getFilesPath()
            )
            rv_feedback.layoutManager = GridLayoutManager(requireContext(),3)
            rv_feedback.adapter = drawAdapter

        }
    }

    private fun getFilesPath(): ArrayList<String>{
        val resultList = ArrayList<String>()
        val path = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        path?.mkdirs()
        val imageList = path?.listFiles()
        if (imageList != null) {
            for (imagePath in imageList){
                resultList.add(imagePath.absolutePath)
            }
        }
        return resultList
    }

    private fun updateRecyclerView(uri: Uri) {
        drawAdapter.addItem(uri)
    }

    private fun saveImage(bitmap: Bitmap, fileName: String) {
        val path =activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.e("path",path.toString())
        val file = File(path, "$fileName.png")
        path?.mkdirs()
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        outputStream.flush()
        outputStream.close()
        updateRecyclerView(Uri.fromFile(file))
    }


    private fun showSaveDialog(bitmap: Bitmap) {
        val alertDialog = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_save, null)
        alertDialog.setView(dialogView)
        val fileNameEditText: EditText = dialogView.findViewById(R.id.editText_file_name)
        val filename = UUID.randomUUID().toString()
        fileNameEditText.setSelectAllOnFocus(true)
        fileNameEditText.setText(filename)
        alertDialog.setTitle("Save Drawing")
            .setPositiveButton("ok") { _, _ -> saveImage(bitmap,fileNameEditText.text.toString()) }
            .setNegativeButton("Cancel") { _, _ -> }

        val dialog = alertDialog.create()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK) {
            when(requestCode){
                REQUEST_CODE_DRAW -> {
                    val result= data.getByteArrayExtra("bitmap")
                    val bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)
                    val filename = UUID.randomUUID().toString()
                    saveImage(bitmap,filename)
                    //showSaveDialog(bitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    drawAdapter = DrawAdapter(requireContext(),getFilesPath())
                    rv_feedback.adapter = drawAdapter
                }else{
                    activity?.finish()
                }
                return
            }
            else -> {}
        }
    }


}