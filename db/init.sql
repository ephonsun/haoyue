

CREATE DATABASE IF NOT EXISTS hymarket DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS address;
CREATE TABLE address (
  id int(11) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  receiver varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS after_sale;
CREATE TABLE after_sale (
  id int(11) NOT NULL AUTO_INCREMENT,
  is_agree varchar(10) DEFAULT NULL,
  message text,
  open_id varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  order_id int(11) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  pics text,
  form_id varchar(255) DEFAULT NULL,
  active bit(1) NOT NULL,
  active_buyer bit(1) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES orders (id)
);

DROP TABLE IF EXISTS basiccomments;
CREATE TABLE basiccomments (
  id int(11) NOT NULL AUTO_INCREMENT,
  comments varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  pro_id varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  wxphoto varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS cashtickets;
CREATE TABLE cashtickets (
  id int(11) NOT NULL AUTO_INCREMENT,
  cash varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  expire_date datetime DEFAULT NULL,
  full_cash varchar(255) DEFAULT NULL,
  isuse bit(1) NOT NULL,
  open_id varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS collections;
CREATE TABLE collections (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_date date DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  pid int(11) NOT NULL,
  seller_id varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  wxpic varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  images varchar(255) DEFAULT NULL,
  level varchar(255) DEFAULT NULL,
  message varchar(255) DEFAULT NULL,
  order_id int(11) DEFAULT NULL,
  reversion varchar(255) DEFAULT NULL,
  seller_id int(11) DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  pid varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  wxpic varchar(255) DEFAULT NULL,
  cutwxname varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS coupons;
CREATE TABLE coupons (
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
  id int(11) NOT NULL AUTO_INCREMENT,
  open_id varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  wxpic varchar(255) DEFAULT NULL,
  expense double NOT NULL,
  PRIMARY KEY (id)
);


DROP TABLE IF EXISTS customer_pro_types;
CREATE TABLE customer_pro_types (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  pcode varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS deliver;
CREATE TABLE deliver (
  id int(11) NOT NULL AUTO_INCREMENT,
  dcode varchar(50) DEFAULT NULL,
  dename varchar(255) DEFAULT NULL,
  dname varchar(20) DEFAULT NULL,
  price double DEFAULT NULL,
  account double DEFAULT NULL,
  destination varchar(255) DEFAULT NULL,
  more_account double DEFAULT NULL,
  more_price double NOT NULL,
  origin_address varchar(255) DEFAULT NULL,
  price_type varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS dictionarys;
CREATE TABLE dictionarys (
  id int(11) NOT NULL AUTO_INCREMENT,
  buyers int(11) DEFAULT NULL,
  create_date date DEFAULT NULL,
  product_id int(11) DEFAULT NULL,
  seller_id int(11) DEFAULT NULL,
  turnover double(10,2) DEFAULT NULL,
  views int(11) DEFAULT NULL,
  visitors int(11) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS leave_message;
CREATE TABLE leave_message (
  id int(11) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  message text,
  name varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS luckdraw;
CREATE TABLE luckdraw (
  id int(11) NOT NULL AUTO_INCREMENT,
  all_number int(11) NOT NULL,
  create_date datetime DEFAULT NULL,
  lack_number varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  join_number int(11) NOT NULL,
  joiners text,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS members;
CREATE TABLE members (
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  discount varchar(255) DEFAULT NULL,
  leavel varchar(255) DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  total_consume varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  comment1 varchar(255) DEFAULT NULL,
  comment2 varchar(255) DEFAULT NULL,
  comment3 varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id int(11) NOT NULL AUTO_INCREMENT,
  active bit(1) NOT NULL,
  amount int(11) NOT NULL,
  create_date datetime DEFAULT NULL,
  customer_id int(11) DEFAULT NULL,
  invoice_type varchar(255) DEFAULT NULL,
  is_apply_return bit(1) NOT NULL,
  leave_message varchar(255) DEFAULT NULL,
  old_price double DEFAULT NULL,
  order_code varchar(255) DEFAULT NULL,
  pay_date datetime DEFAULT NULL,
  pay_type varchar(255) DEFAULT NULL,
  price double DEFAULT NULL,
  seller_id int(11) DEFAULT NULL,
  seller_name varchar(255) DEFAULT NULL,
  state varchar(255) DEFAULT NULL,
  total_price double DEFAULT NULL,
  address_id int(11) DEFAULT NULL,
  comment_id int(11) DEFAULT NULL,
  deliver_id int(11) DEFAULT NULL,
  leave_message_seller varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  is_luck bit(1) NOT NULL,
  is_luck_draw bit(1) NOT NULL,
  is_luck_draw_end bit(1) NOT NULL,
  luckcode varchar(255) DEFAULT NULL,
  iscomment bit(1) NOT NULL,
  active_seller bit(1) NOT NULL,
  latest_date datetime DEFAULT NULL,
  PRIMARY KEY (id),
   FOREIGN KEY (deliver_id) REFERENCES deliver (id),
   FOREIGN KEY (address_id) REFERENCES address (id),
   FOREIGN KEY (comment_id) REFERENCES comments (id)
);

DROP TABLE IF EXISTS paydeals;
CREATE TABLE paydeals (
  id int(11) NOT NULL AUTO_INCREMENT,
  app_id varchar(255) DEFAULT NULL,
  date datetime DEFAULT NULL,
  mch_id varchar(255) DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  return_code varchar(255) DEFAULT NULL,
  total_fee varchar(255) DEFAULT NULL,
  transaction_id varchar(255) DEFAULT NULL,
  out_trade_no varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
  id int(11) NOT NULL AUTO_INCREMENT,
  active bit(1) NOT NULL,
  brand varchar(255) DEFAULT NULL,
  create_date date DEFAULT NULL,
  deliver_price varchar(255) DEFAULT NULL,
  design_pic varchar(255) DEFAULT NULL,
  images text,
  index_images text,
  material_quality varchar(255) DEFAULT NULL,
  model varchar(255) DEFAULT NULL,
  month_sale int(11) DEFAULT '0',
  p_number varchar(255) DEFAULT NULL,
  p_style varchar(255) DEFAULT NULL,
  pattern varchar(255) DEFAULT NULL,
  pdesc text,
  pname varchar(255) DEFAULT NULL,
  ptype_name varchar(255) DEFAULT NULL,
  pvideo varchar(255) DEFAULT NULL,
  seller_id int(11) DEFAULT NULL,
  seller_name varchar(255) DEFAULT NULL,
  send_address varchar(255) DEFAULT NULL,
  thumbsup int(11) DEFAULT NULL,
  deliever_name varchar(255) DEFAULT NULL,
  dname varchar(255) DEFAULT NULL,
  invoice_type varchar(255) DEFAULT NULL,
  pcode varchar(255) DEFAULT NULL,
  shopcar_count int(11) DEFAULT NULL,
  is_luck_draw bit(1) NOT NULL,
  is_luck_draw_end bit(1) NOT NULL,
  views int(11) NOT NULL,
  suffix varchar(255) DEFAULT NULL,
  qrcode varchar(255) DEFAULT NULL,
  show_date datetime DEFAULT NULL,
  issecondkill bit(1) NOT NULL,
  second_kill_end datetime DEFAULT NULL,
  second_kill_start datetime DEFAULT NULL,
  weight double NOT NULL,
  PRIMARY KEY (id)
);


DROP TABLE IF EXISTS protypes;
CREATE TABLE protypes (
  id int(11) NOT NULL AUTO_INCREMENT,
  active bit(1) NOT NULL,
  amount int(11) DEFAULT NULL,
  color varchar(255) DEFAULT NULL,
  price_new double DEFAULT NULL,
  price_old double DEFAULT NULL,
  product_id int(11) DEFAULT NULL,
  seller_id int(11) DEFAULT NULL,
  size varchar(255) DEFAULT NULL,
  discount_price double DEFAULT NULL,
  is_discount bit(1) NOT NULL,
  second_kill_price double DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ptypenames;
CREATE TABLE ptypenames (
  id int(11) NOT NULL AUTO_INCREMENT,
  ptypename varchar(255) DEFAULT NULL,
  seller_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS sellers;
CREATE TABLE sellers (
  seller_id int(11) NOT NULL AUTO_INCREMENT,
  app_id varchar(255) DEFAULT NULL,
  authority varchar(255) DEFAULT NULL,
  banners text,
  expire_date datetime DEFAULT NULL,
  index_title varchar(255) DEFAULT NULL,
  is_active bit(1) NOT NULL,
  iscoupon bit(1) NOT NULL,
  max_file_size int(11) DEFAULT NULL,
  pall_title varchar(255) DEFAULT NULL,
  pdesc_title varchar(255) DEFAULT NULL,
  seller_email varchar(20) DEFAULT NULL,
  seller_name varchar(20) DEFAULT NULL,
  seller_pass varchar(255) DEFAULT NULL,
  seller_phone varchar(20) DEFAULT NULL,
  upload_file_size int(11) DEFAULT NULL,
  videos varchar(255) DEFAULT NULL,
  lunbo varchar(255) DEFAULT NULL,
  online_code varchar(255) DEFAULT NULL,
  is_member bit(1) NOT NULL,
  key1 varchar(255) DEFAULT NULL,
  mch_id varchar(255) DEFAULT NULL,
  mach_id varchar(255) DEFAULT NULL,
  file_payback varchar(255) DEFAULT NULL,
  secret varchar(255) DEFAULT NULL,
  template_daily varchar(255) DEFAULT NULL,
  template_downprice varchar(255) DEFAULT NULL,
  template_pay varchar(255) DEFAULT NULL,
  PRIMARY KEY (seller_id),
  UNIQUE KEY seller_phone (seller_phone),
  UNIQUE KEY seller_name (seller_name),
  UNIQUE KEY seller_email (seller_email)
);

DROP TABLE IF EXISTS shopcar;
CREATE TABLE shopcar (
  id int(11) NOT NULL AUTO_INCREMENT,
  customer_id int(11) DEFAULT NULL,
  seller_id int(11) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  active bit(1) NOT NULL,
  end_date datetime DEFAULT NULL,
  form_id varchar(255) DEFAULT NULL,
  form_id2 varchar(255) DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS shopcardetail;
CREATE TABLE shopcardetail (
  id int(11) NOT NULL AUTO_INCREMENT,
  amount int(11) DEFAULT NULL,
  produts_type_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (produts_type_id) REFERENCES protypes (id)
);

DROP TABLE IF EXISTS super_admin;
CREATE TABLE super_admin (
  id int(11) NOT NULL AUTO_INCREMENT,
  admin_name varchar(255) DEFAULT NULL,
  admin_pass varchar(255) DEFAULT NULL,
  admin_phone varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);


-- 团实惠

DROP TABLE IF EXISTS t_address;
CREATE TABLE t_address (
  id int(11) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  open_id varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  receiver varchar(255) DEFAULT NULL,
  sale_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS t_after_sale;
CREATE TABLE t_after_sale (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  form_id varchar(255) DEFAULT NULL,
  is_agree varchar(255) DEFAULT NULL,
  message text,
  open_id varchar(255) DEFAULT NULL,
  pics text,
  sale_id varchar(255) DEFAULT NULL,
  order_id int(11) DEFAULT NULL,
  tuan_orders_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES t_orders (id),
  FOREIGN KEY (tuan_orders_id) REFERENCES t_tuanorders (id)
);

DROP TABLE IF EXISTS t_collections;
CREATE TABLE t_collections (
  id int(11) NOT NULL AUTO_INCREMENT,
  cid varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  sale_id varchar(255) DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS t_comment;
CREATE TABLE t_comment (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  message text,
  open_id varchar(255) DEFAULT NULL,
  pics text,
  replay varchar(255) DEFAULT NULL,
  sale_id varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  wxpic varchar(255) DEFAULT NULL,
  t_products_id int(11) DEFAULT NULL,
  t_products_types_id int(11) DEFAULT NULL,
  level varchar(255) DEFAULT NULL,
  cutwxname varchar(255) DEFAULT NULL,
  ordercode varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (t_products_id) REFERENCES t_products (id),
  FOREIGN KEY (t_products_types_id) REFERENCES t_products_type (id)
);

DROP TABLE IF EXISTS t_coupon;
CREATE TABLE t_coupon (
  id int(11) NOT NULL AUTO_INCREMENT,
  active bit(1) NOT NULL,
  create_date datetime DEFAULT NULL,
  end_date datetime DEFAULT NULL,
  form_id varchar(255) DEFAULT NULL,
  isuse bit(1) NOT NULL,
  money int(11) NOT NULL,
  open_id varchar(255) DEFAULT NULL,
  sale_id varchar(255) DEFAULT NULL,
  wxname varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);


DROP TABLE IF EXISTS t_deliver_template;
CREATE TABLE t_deliver_template (
  id int(11) NOT NULL AUTO_INCREMENT,
  count varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  destination_address varchar(255) DEFAULT NULL,
  more_count varchar(255) DEFAULT NULL,
  more_price double NOT NULL,
  price double NOT NULL,
  sale_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS t_deliver_templates;
CREATE TABLE t_deliver_templates (
  id int(11) NOT NULL AUTO_INCREMENT,
  count varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  deliver_type varchar(255) DEFAULT NULL,
  dname varchar(255) DEFAULT NULL,
  more_count varchar(255) DEFAULT NULL,
  more_price double NOT NULL,
  price double NOT NULL,
  price_type varchar(255) DEFAULT NULL,
  sale_id varchar(255) DEFAULT NULL,
  send_address varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS t_delivers;
CREATE TABLE t_delivers (
  id int(11) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  dcode varchar(255) DEFAULT NULL,
  dname varchar(255) DEFAULT NULL,
  open_id1 varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  receiver varchar(255) DEFAULT NULL,
  sale_id1 varchar(255) DEFAULT NULL,
  send_date datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

