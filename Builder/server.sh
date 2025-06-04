#cd /home/data/git/LibreraReader/docs
cd /home/dev/git/SlapImage/what-is-new
mogrify -quality 85 -resize 600 *.png

cd /home/dev/git/SlapImage/docs
cd /Users/dev/git/SlapImage/docs

if [ "$1" == "init" ]; then
bundle init
bundle add webrick
bundle add jekyll-watch
bundle add kramdown-parser-gfm
fi

jekyll serve
