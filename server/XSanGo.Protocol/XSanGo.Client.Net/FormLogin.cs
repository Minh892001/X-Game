using com.XSanGo.Protocol;
using System;
using System.Threading;
using System.Windows.Forms;
using System.Xml;

namespace XSanGo.Client.Net
{
    public partial class FormLogin : Form
    {

        public FormLogin()
        {
            InitializeComponent();

            this.FormClosed += FormLogin_FormClosed;

            listView1.ItemActivate += delegate(object sender, EventArgs e)
            {
                //listView1.SelectedItems[0].
                var form = new FormGame(listView1.SelectedItems[0].Tag as IceManager);
                form.Show();
            };
        }

        void FormLogin_FormClosed(object sender, FormClosedEventArgs e)
        {
            foreach (ListViewItem item in listView1.Items)
            {
                IceManager mgr = item.Tag as IceManager;

                mgr.disconnect();
                mgr.onStateChange = null;
            }
        }

        private void FormLogin_Load(object sender, EventArgs e)
        {
            XmlDocument doc = new XmlDocument();
            doc.Load("config.xml");

            Ice.InitializationData initData = new Ice.InitializationData();
            initData.properties = Ice.Util.createProperties();
            initData.properties.load("ice.config");

            foreach (XmlNode cfg in doc.GetElementsByTagName("account"))
            {
                IceManager app = new IceManager();
                app.username = cfg.Attributes["username"].Value;
                app.password = cfg.Attributes["password"].Value;
                app.Initialize(initData);

                var item = listView1.Items.Add(app.username);
                item.SubItems.Add(app.state.ToString());
                item.SubItems.Add("");
                item.Tag = app;
            }
        }

        delegate Ice.AsyncResult TestAction(IceManager mgr);
        private void DoTest(TestAction action)
        {
            foreach (ListViewItem item in listView1.Items)
            {
                IceManager selected = item.Tag as IceManager;

                if (selected.state != IceManager.State.Loaded)
                {
                    return;
                }

                selected.lastException = null;
                selected.lastRequest = action(selected);
                selected.lastRequest.whenCompleted(delegate(Ice.Exception ex)
                {
                    selected.lastException = ex;
                });
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            foreach (ListViewItem item in listView1.Items)
            {
                IceManager selected = item.Tag as IceManager;
                selected.connect();
            }
        }

        //排行榜
        private void buttonRank_Click(object sender, EventArgs e)
        {
            DoTest(delegate(IceManager mgr)
            {
                return mgr.rank.begin_selRankListCombat();
            });
        }

        private void buttonChat_Click(object sender, EventArgs e)
        {
            DoTest(delegate(IceManager mgr)
            {
                return mgr.chat.begin_speak("{\"attachs\":[{\"id\":0,\"content\":[1]}],\"type\":1,\"content\":\"压力测试\",\"channel\":\"World\"}");
            });
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            foreach (ListViewItem item in listView1.Items)
            {
                IceManager selected = item.Tag as IceManager;

                if (item.SubItems[1].Text != selected.state.ToString())
                    item.SubItems[1].Text = selected.state.ToString();

                string requestState = "";
                if (selected.lastRequest == null)
                {
                    requestState = "";
                    continue;
                }

                if (selected.lastRequest.IsCompleted)
                {
                    if (selected.lastException == null)
                    {
                        requestState = "完成";
                    }
                    else
                    {
                        requestState = "异常";
                    }
                }
                else if (selected.lastRequest.isSent())
                {
                    requestState = "已发送";
                }
                else
                {
                    requestState = "请求...";
                }

                if (requestState != item.SubItems[2].Text)
                {
                    item.SubItems[2].Text = requestState;
                }
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            DoTest(delegate(IceManager mgr)
            {
                return mgr.pvp.begin_selHundredRank();
            });
        }
    }
}
 