namespace XSanGo.Client.Net
{
    partial class FormGame
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btnTest = new System.Windows.Forms.Button();
            this.txtPlan = new System.Windows.Forms.TextBox();
            this.btnSetCallback = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnTest
            // 
            this.btnTest.Location = new System.Drawing.Point(237, 315);
            this.btnTest.Name = "btnTest";
            this.btnTest.Size = new System.Drawing.Size(114, 23);
            this.btnTest.TabIndex = 0;
            this.btnTest.Text = "同步服务器时间";
            this.btnTest.UseVisualStyleBackColor = true;
            this.btnTest.Click += new System.EventHandler(this.btnTest_Click);
            // 
            // txtPlan
            // 
            this.txtPlan.Location = new System.Drawing.Point(38, 32);
            this.txtPlan.Multiline = true;
            this.txtPlan.Name = "txtPlan";
            this.txtPlan.Size = new System.Drawing.Size(395, 241);
            this.txtPlan.TabIndex = 1;
            // 
            // btnSetCallback
            // 
            this.btnSetCallback.Location = new System.Drawing.Point(111, 315);
            this.btnSetCallback.Name = "btnSetCallback";
            this.btnSetCallback.Size = new System.Drawing.Size(75, 23);
            this.btnSetCallback.TabIndex = 2;
            this.btnSetCallback.Text = "设置回调";
            this.btnSetCallback.UseVisualStyleBackColor = true;
            this.btnSetCallback.Click += new System.EventHandler(this.btnSetCallback_Click);
            // 
            // FormGame
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(466, 359);
            this.Controls.Add(this.btnSetCallback);
            this.Controls.Add(this.txtPlan);
            this.Controls.Add(this.btnTest);
            this.Name = "FormGame";
            this.Text = "FormGame";
            this.Load += new System.EventHandler(this.FormGame_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnTest;
        private System.Windows.Forms.TextBox txtPlan;
        private System.Windows.Forms.Button btnSetCallback;
    }
}