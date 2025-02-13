using com.XSanGo.Protocol;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace XSanGo.Client.Net
{
    public partial class FormGame : Form
    {
        public IceManager icemanager;

        public FormGame(IceManager mgr)
        {
            icemanager = mgr;
            InitializeComponent();
        }

        private void btnTest_Click(object sender, EventArgs e)
        {
            icemanager._session.begin_syncTime().whenCompleted(ie => MessageBox.Show("请求失败。"));
        }

        private void btnSetCallback_Click(object sender, EventArgs e)
        {
        //    txtPlan.Text += (icemanager._helper.objectAdapter().getCommunicator().proxyToString(callbackPrx) + " setting callback" + Environment.NewLine);
        //    icemanager._session.begin_setCallback(callbackPrx)
        //            .whenCompleted(() => MessageBox.Show("设置成功。"), ie => MessageBox.Show("设置失败。"));
        }

        internal void OnTimeSyncronized(string note)
        {
            txtPlan.Text += note;
            txtPlan.Text += Environment.NewLine;
        }

        private void toolStripButton1_Click(object sender, EventArgs e)
        {

        }

        private void FormGame_Load(object sender, EventArgs e)
        {
            txtPlan.Text = icemanager.lastException.ToString();
        }
    }
}
