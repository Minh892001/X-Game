namespace XSanGo.Client.Net
{
    partial class FormLogin
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.listView1 = new System.Windows.Forms.ListView();
            this.username = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.state = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.button1 = new System.Windows.Forms.Button();
            this.buttonRank = new System.Windows.Forms.Button();
            this.buttonChat = new System.Windows.Forms.Button();
            this.button4 = new System.Windows.Forms.Button();
            this.button5 = new System.Windows.Forms.Button();
            this.RequestState = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.SuspendLayout();
            // 
            // listView1
            // 
            this.listView1.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.username,
            this.state,
            this.RequestState});
            this.listView1.FullRowSelect = true;
            this.listView1.HideSelection = false;
            this.listView1.Location = new System.Drawing.Point(12, 37);
            this.listView1.Name = "listView1";
            this.listView1.Size = new System.Drawing.Size(695, 352);
            this.listView1.TabIndex = 7;
            this.listView1.UseCompatibleStateImageBehavior = false;
            this.listView1.View = System.Windows.Forms.View.Details;
            // 
            // username
            // 
            this.username.Text = "用户名";
            this.username.Width = 398;
            // 
            // state
            // 
            this.state.Text = "连接状态";
            this.state.Width = 200;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(12, -1);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 32);
            this.button1.TabIndex = 7;
            this.button1.Text = "登录";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // buttonRank
            // 
            this.buttonRank.Location = new System.Drawing.Point(93, -1);
            this.buttonRank.Name = "buttonRank";
            this.buttonRank.Size = new System.Drawing.Size(78, 32);
            this.buttonRank.TabIndex = 8;
            this.buttonRank.Text = "排行榜";
            this.buttonRank.UseVisualStyleBackColor = true;
            this.buttonRank.Click += new System.EventHandler(this.buttonRank_Click);
            // 
            // buttonChat
            // 
            this.buttonChat.Location = new System.Drawing.Point(177, -1);
            this.buttonChat.Name = "buttonChat";
            this.buttonChat.Size = new System.Drawing.Size(75, 32);
            this.buttonChat.TabIndex = 9;
            this.buttonChat.Text = "聊天群发";
            this.buttonChat.UseVisualStyleBackColor = true;
            this.buttonChat.Click += new System.EventHandler(this.buttonChat_Click);
            // 
            // button4
            // 
            this.button4.Location = new System.Drawing.Point(258, -1);
            this.button4.Name = "button4";
            this.button4.Size = new System.Drawing.Size(75, 32);
            this.button4.TabIndex = 10;
            this.button4.Text = "竞技场";
            this.button4.UseVisualStyleBackColor = true;
            this.button4.Click += new System.EventHandler(this.button4_Click);
            // 
            // button5
            // 
            this.button5.Location = new System.Drawing.Point(340, -1);
            this.button5.Name = "button5";
            this.button5.Size = new System.Drawing.Size(75, 32);
            this.button5.TabIndex = 11;
            this.button5.Text = "副本";
            this.button5.UseVisualStyleBackColor = true;
            // 
            // RequestState
            // 
            this.RequestState.Text = "请求状态";
            // 
            // timer1
            // 
            this.timer1.Enabled = true;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // FormLogin
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(719, 401);
            this.Controls.Add(this.button5);
            this.Controls.Add(this.button4);
            this.Controls.Add(this.buttonChat);
            this.Controls.Add(this.buttonRank);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.listView1);
            this.Name = "FormLogin";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.FormLogin_Load);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListView listView1;
        private System.Windows.Forms.ColumnHeader username;
        private System.Windows.Forms.ColumnHeader state;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button buttonRank;
        private System.Windows.Forms.Button buttonChat;
        private System.Windows.Forms.Button button4;
        private System.Windows.Forms.Button button5;
        private System.Windows.Forms.ColumnHeader RequestState;
        private System.Windows.Forms.Timer timer1;
    }
}

